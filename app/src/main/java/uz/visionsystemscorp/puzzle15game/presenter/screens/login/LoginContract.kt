package uz.visionsystemscorp.puzzle15game.presenter.screens.login

import org.orbitmvi.orbit.OrbitContainerHost

interface LoginContract {

    interface LoginViewModel : OrbitContainerHost<LoginUiState, LoginUiState, SideEffect> {
        fun onEventDispatcher(event: LoginEvent)
    }

    sealed interface LoginEvent {
        data object GoogleClicked : LoginEvent
        data object GuestClicked : LoginEvent

        /** Results of the account picker the screen opened for [SideEffect.LaunchGoogleSignIn]. */
        data class GoogleTokenReceived(val idToken: String) : LoginEvent
        data object GoogleCancelled : LoginEvent
        data object GoogleFailed : LoginEvent
    }

    data class LoginUiState(
        val isOnboarding: Boolean = true,
        val isLoading: Boolean = false,
    )

    sealed interface SideEffect {
        /** The account picker needs an Activity, so the screen shows it. */
        data object LaunchGoogleSignIn : SideEffect
        data object ShowSignInError : SideEffect
    }

    interface Direction {
        fun openName()
        fun openMain()
        fun back()
    }
}
