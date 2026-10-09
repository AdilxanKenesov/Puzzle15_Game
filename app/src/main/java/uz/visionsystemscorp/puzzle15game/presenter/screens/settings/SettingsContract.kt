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
        data object DeleteAccountClicked : SettingsEvent
        data object DeleteConfirmed : SettingsEvent
        data object DeleteDismissed : SettingsEvent

        /** Results of the Google sign-in the screen opened for [SideEffect.LaunchGoogleReauth]. */
        data class ReauthTokenReceived(val idToken: String) : SettingsEvent
        data object ReauthCancelled : SettingsEvent
    }

    data class SettingsUiState(
        val settings: UserSettings = UserSettings(),
        val playerName: String = "",
        val isSignedIn: Boolean = false,
        val hasAccount: Boolean = false,
        val showDeleteConfirm: Boolean = false,
        val isDeleting: Boolean = false,
    )

    sealed interface SideEffect {
        /** Firebase asked for a fresh Google sign-in before deleting the account. */
        data object LaunchGoogleReauth : SideEffect
        data object ShowDeleteError : SideEffect
    }

    interface Direction {
        fun openNameEditor()
        fun openLogin()
        fun back()

        /** After signing out or deleting the account the app starts over from the sign-in screen. */
        fun restartOnboarding()
    }
}
