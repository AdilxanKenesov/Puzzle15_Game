package uz.visionsystemscorp.puzzle15game.presenter.screens.win

import org.orbitmvi.orbit.OrbitContainerHost
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult

interface WinContract {

    interface WinViewModel : OrbitContainerHost<WinUiState, WinUiState, SideEffect> {
        fun onEventDispatcher(event: WinEvent)
    }

    sealed interface WinEvent {
        data object HomeClicked : WinEvent
        data object ReplayClicked : WinEvent
        data object RecordsClicked : WinEvent
    }

    data class WinUiState(val result: GameResult)

    sealed interface SideEffect

    interface Direction {
        fun backToMain()
        fun replay(size: BoardSize)
        fun openRecords(size: BoardSize)
    }
}
