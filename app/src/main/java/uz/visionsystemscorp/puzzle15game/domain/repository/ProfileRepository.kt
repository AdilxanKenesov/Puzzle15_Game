package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile

interface ProfileRepository {
    val profile: StateFlow<PlayerProfile>

    fun setName(name: String)

    /**
     * Akkauntda saqlangan ismni oladi, akkauntda ism bo'lmasa lokal ismni yuklaydi.
     * Tizimga kirilmagan yoki internet yo'q bo'lsa hech narsa qilmaydi.
     */
    suspend fun syncName()

    fun isOnboardingDone(): Boolean

    fun setOnboardingDone()

    fun reset()
}
