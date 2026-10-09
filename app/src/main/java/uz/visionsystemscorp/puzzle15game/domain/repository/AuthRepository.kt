package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.visionsystemscorp.puzzle15game.domain.model.Account

interface AuthRepository {
    /** Null while signed out. */
    val account: StateFlow<Account?>

    /** Keeps the current session if there is one, otherwise starts an anonymous one. */
    suspend fun signInAsGuest(): Result<Account>

    /**
     * Upgrades a guest to the Google account behind [idToken], keeping its uid. If that Google
     * account is already in use, the guest's cloud data is dropped and the session switches to it.
     */
    suspend fun signInWithGoogle(idToken: String): Result<Account>

    suspend fun signOut()

    /** Starts a guest session in the background if one was lost (e.g. first sign-in happened offline). */
    fun restoreSession()
}
