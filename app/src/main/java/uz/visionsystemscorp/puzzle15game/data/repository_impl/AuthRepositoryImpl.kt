package uz.visionsystemscorp.puzzle15game.data.repository_impl

import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uz.visionsystemscorp.puzzle15game.data.source.local.pref.SharedManager
import uz.visionsystemscorp.puzzle15game.data.source.remote.firestore.FirestoreSource
import uz.visionsystemscorp.puzzle15game.di.ApplicationScope
import uz.visionsystemscorp.puzzle15game.domain.model.Account
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirestoreSource,
    private val credentialManager: CredentialManager,
    private val sharedManager: SharedManager,
    @ApplicationScope private val scope: CoroutineScope,
) : AuthRepository {

    private val state = MutableStateFlow(auth.currentUser?.toAccount())

    override val account: StateFlow<Account?> = state.asStateFlow()

    override suspend fun signInAsGuest(): Result<Account> = runCatching {
        val user = auth.currentUser ?: checkNotNull(auth.signInAnonymously().await().user)
        onSignedIn(user)
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Account> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val current = auth.currentUser
        val user = if (current?.isAnonymous == true) {
            try {
                checkNotNull(current.linkWithCredential(credential).await().user)
            } catch (e: FirebaseAuthUserCollisionException) {
                // This Google account already has its own records: abandon the guest and switch to it.
                // The guest's local results are re-published under the Google account afterwards.
                runCatching { firestore.deleteAccountData(current.uid, BoardSize.entries.map { it.dimension }) }
                runCatching { current.delete().await() }
                checkNotNull(auth.signInWithCredential(e.updatedCredential ?: credential).await().user)
            }
        } else {
            checkNotNull(auth.signInWithCredential(credential).await().user)
        }
        onSignedIn(user)
    }.onFailure { Log.w(TAG, "Firebase Google sign-in failed", it) }

    override suspend fun signOut() {
        sharedManager.signedOut = true
        auth.signOut()
        state.value = null
        runCatching { credentialManager.clearCredentialState(ClearCredentialStateRequest()) }
    }

    override fun restoreSession() {
        if (auth.currentUser != null || sharedManager.signedOut) return
        scope.launch { signInAsGuest() }
    }

    private fun onSignedIn(user: FirebaseUser): Account {
        sharedManager.signedOut = false
        // Linking a guest to Google keeps the uid, so auth listeners would not report it; publish explicitly.
        return user.toAccount().also { state.value = it }
    }

    private fun FirebaseUser.toAccount() = Account(uid = uid, isGuest = isAnonymous)

    private companion object {
        const val TAG = "GoogleSignIn"
    }
}
