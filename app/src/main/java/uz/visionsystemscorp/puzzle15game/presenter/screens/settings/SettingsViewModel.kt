package uz.visionsystemscorp.puzzle15game.presenter.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion
import uz.visionsystemscorp.puzzle15game.domain.usecase.settings.SettingsUseCase
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsContract.SettingsEvent

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val direction: SettingsContract.Direction,
    private val settingsUseCase: SettingsUseCase,
) : ViewModel(), SettingsContract.SettingsViewModel {

    override val container = orbitContainer<SettingsContract.SettingsUiState, SettingsContract.SideEffect>(
        SettingsContract.SettingsUiState(),
    )

    init {
        intent {
            settingsUseCase.observe().collect { data ->
                reduce {
                    state.copy(
                        settings = data.settings,
                        playerName = data.playerName,
                        isSignedIn = data.isSignedIn,
                        hasAccount = data.hasAccount,
                    )
                }
            }
        }
    }

    override fun onEventDispatcher(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.MusicChanged -> settingsUseCase.setMusic(event.on)
            is SettingsEvent.SoundChanged -> settingsUseCase.setSound(event.on)
            is SettingsEvent.VibrationChanged -> settingsUseCase.setVibration(event.on)
            SettingsEvent.EditNameClicked -> direction.openNameEditor()
            SettingsEvent.AccountClicked -> intent {
                if (state.isSignedIn) settingsUseCase.signOut() else direction.openLogin()
            }
            SettingsEvent.CloseClicked -> direction.back()
            SettingsEvent.DeleteAccountClicked -> intent { reduce { state.copy(showDeleteConfirm = true) } }
            SettingsEvent.DeleteDismissed -> intent {
                if (!state.isDeleting) reduce { state.copy(showDeleteConfirm = false) }
            }
            SettingsEvent.DeleteConfirmed -> deleteAccount(googleIdToken = null)
            is SettingsEvent.ReauthTokenReceived -> deleteAccount(event.idToken)
            SettingsEvent.ReauthCancelled -> intent { reduce { state.copy(isDeleting = false) } }
        }
    }

    private fun deleteAccount(googleIdToken: String?) = intent {
        reduce { state.copy(isDeleting = true) }
        when (settingsUseCase.deleteAccount(googleIdToken)) {
            AccountDeletion.DELETED -> direction.restartOnboarding()
            AccountDeletion.NEEDS_REAUTH -> postSideEffect(SettingsContract.SideEffect.LaunchGoogleReauth)
            AccountDeletion.FAILED -> {
                reduce { state.copy(isDeleting = false) }
                postSideEffect(SettingsContract.SideEffect.ShowDeleteError)
            }
        }
    }
}
