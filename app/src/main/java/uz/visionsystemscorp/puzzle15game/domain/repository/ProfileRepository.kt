package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile

interface ProfileRepository {
    val profile: StateFlow<PlayerProfile>

    fun setName(name: String)

    /**
     * Adopts the name stored in the signed-in account, or uploads the local one if the account has none.
     * Does nothing while signed out or offline.
     */
    suspend fun syncName()

    fun isOnboardingDone(): Boolean

    fun setOnboardingDone()
}
