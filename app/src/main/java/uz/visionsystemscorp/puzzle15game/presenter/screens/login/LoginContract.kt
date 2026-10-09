package uz.visionsystemscorp.puzzle15game.presenter.screens.login

import org.orbitmvi.orbit.OrbitContainerHost

interface LoginContract {

    interface LoginViewModel : OrbitContainerHost<LoginUiState, LoginUiState, SideEffect> {
        fun onEventDispatcher(event: LoginEvent)
    }

    sealed interface LoginEvent {
        data object GoogleClicked : LoginEvent
        data object GuestClicked : LoginEvent

        data class GoogleTokenReceived(val idToken: String) : LoginEvent
        data object GoogleCancelled : LoginEvent
        data object GoogleFailed : LoginEvent
    }

    data class LoginUiState(
        val isOnboarding: Boolean = true,
        val isLoading: Boolean = false,
    )

    sealed interface SideEffect {
        /** Akkaunt tanlash oynasiga Activity kerak, shuning uchun uni ekran ochadi. */
        data object LaunchGoogleSignIn : SideEffect
        data object ShowSignInError : SideEffect
    }

    interface Direction {
        fun openName()
        fun openMain()
        fun back()
    }
}
