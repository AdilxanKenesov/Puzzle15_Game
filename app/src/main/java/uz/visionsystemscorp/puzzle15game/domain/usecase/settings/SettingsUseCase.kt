package uz.visionsystemscorp.puzzle15game.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion
import uz.visionsystemscorp.puzzle15game.domain.model.SettingsData

interface SettingsUseCase {
    fun observe(): Flow<SettingsData>

    fun setMusic(on: Boolean)

    fun setSound(on: Boolean)

    fun setVibration(on: Boolean)

    /** Signs out and resets this phone's progress, so the next player starts clean. */
    suspend fun signOut()

    /** Deletes the account and, once that succeeds, all local progress so the app starts over. */
    suspend fun deleteAccount(googleIdToken: String? = null): AccountDeletion
}
