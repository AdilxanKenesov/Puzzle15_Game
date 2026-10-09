package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.visionsystemscorp.puzzle15game.domain.model.UserSettings

interface SettingsRepository {
    val settings: StateFlow<UserSettings>

    fun update(transform: (UserSettings) -> UserSettings)
}
