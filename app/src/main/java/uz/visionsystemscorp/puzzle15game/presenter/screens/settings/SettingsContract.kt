package uz.visionsystemscorp.puzzle15game.presenter.screens.settings

import org.orbitmvi.orbit.OrbitContainerHost
import uz.visionsystemscorp.puzzle15game.domain.model.UserSettings

interface SettingsContract {

    interface SettingsViewModel : OrbitContainerHost<SettingsUiState, SettingsUiState, SideEffect> {
        fun onEventDispatcher(event: SettingsEvent)
    }

    sealed interface SettingsEvent {
        data class MusicChanged(val on: Boolean) : SettingsEvent
        data class SoundChanged(val on: Boolean) : SettingsEvent
        data class VibrationChanged(val on: Boolean) : SettingsEvent
        data object EditNameClicked : SettingsEvent
        data object AccountClicked : SettingsEvent
        data object CloseClicked : SettingsEvent
    }

    data class SettingsUiState(
        val settings: UserSettings = UserSettings(),
        val playerName: String = "",
        val isSignedIn: Boolean = false,
    )

    sealed interface SideEffect

    interface Direction {
        fun openNameEditor()
        fun openLogin()
        fun back()
    }
}
