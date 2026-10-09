package uz.visionsystemscorp.puzzle15game.presenter.screens.splash

import org.orbitmvi.orbit.OrbitContainerHost

interface SplashContract {

    interface SplashViewModel : OrbitContainerHost<SplashUiState, SplashUiState, SideEffect> {
        fun onEventDispatcher(event: SplashEvent)
    }

    sealed interface SplashEvent {
        data object LoadingFinished : SplashEvent
    }

    data object SplashUiState

    sealed interface SideEffect

    interface Direction {
        fun openOnboarding()
        fun openMain()
    }
}
