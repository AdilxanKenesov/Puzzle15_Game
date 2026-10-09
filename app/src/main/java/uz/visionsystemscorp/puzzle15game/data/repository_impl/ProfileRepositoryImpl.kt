package uz.visionsystemscorp.puzzle15game.data.repository_impl

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.visionsystemscorp.puzzle15game.data.source.local.pref.SharedManager
import uz.visionsystemscorp.puzzle15game.data.source.remote.firestore.FirestoreSource
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository

/**
 * Ism telefonda saqlanadi. Google akkauntlarda uning nusxasi `users/{uid}` da ham turadi, shunda
 * qayta o'rnatganda yoki boshqa telefonda ism qaytib keladi. Mehmon uchun bu kerak emas.
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val sharedManager: SharedManager,
    private val authRepository: AuthRepository,
    private val firestore: FirestoreSource,
) : ProfileRepository {

    private val state = MutableStateFlow(PlayerProfile(name = sharedManager.playerName))

    override val profile: StateFlow<PlayerProfile> = state.asStateFlow()

    override fun setName(name: String) {
        setLocalName(name)
        googleUid()?.let { firestore.putName(it, name) }
    }

    override suspend fun syncName() {
        val uid = googleUid() ?: return
        val remote = runCatching { firestore.fetchName(uid) }.getOrElse { return }
        val local = state.value.name
        when {
            remote != null && PlayerProfile.isValidName(remote) -> setLocalName(remote)
            PlayerProfile.isValidName(local) -> firestore.putName(uid, local)
        }
    }

    override fun isOnboardingDone(): Boolean = sharedManager.onboardingDone

    override fun setOnboardingDone() {
        sharedManager.onboardingDone = true
    }

    override fun reset() {
        sharedManager.onboardingDone = false
        setLocalName("")
    }

    private fun setLocalName(name: String) {
        sharedManager.playerName = name
        state.update { it.copy(name = name) }
    }

    private fun googleUid(): String? = authRepository.account.value?.takeUnless { it.isGuest }?.uid
}
