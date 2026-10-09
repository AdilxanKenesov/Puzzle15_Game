package uz.visionsystemscorp.puzzle15game.presenter.screens.info

import org.orbitmvi.orbit.OrbitContainerHost

interface InfoContract {

    interface InfoViewModel : OrbitContainerHost<InfoUiState, InfoUiState, SideEffect> {
        fun onEventDispatcher(event: InfoEvent)
    }

    sealed interface InfoEvent {
        data object CloseClicked : InfoEvent
    }

    data object InfoUiState

    sealed interface SideEffect

    interface Direction {
        fun back()
    }
}
