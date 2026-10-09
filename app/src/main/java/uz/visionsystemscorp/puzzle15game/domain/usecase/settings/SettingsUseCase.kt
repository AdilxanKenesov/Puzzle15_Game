package uz.visionsystemscorp.puzzle15game.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.SettingsData

interface SettingsUseCase {
    fun observe(): Flow<SettingsData>

    fun setMusic(on: Boolean)

    fun setSound(on: Boolean)

    fun setVibration(on: Boolean)

    suspend fun signOut()
}
