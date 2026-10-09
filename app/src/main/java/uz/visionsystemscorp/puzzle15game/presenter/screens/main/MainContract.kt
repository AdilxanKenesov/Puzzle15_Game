package uz.visionsystemscorp.puzzle15game.presenter.screens.main

import org.orbitmvi.orbit.OrbitContainerHost
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize

interface MainContract {

    interface MainViewModel : OrbitContainerHost<MainUiState, MainUiState, SideEffect> {
        fun onEventDispatcher(event: MainEvent)
    }

    sealed interface MainEvent {
        data object PreviousSizeClicked : MainEvent
        data object NextSizeClicked : MainEvent
        data object PlayClicked : MainEvent
        data object RecordsClicked : MainEvent
        data object SettingsClicked : MainEvent
        data object InfoClicked : MainEvent
    }

    data class MainUiState(
        val playerName: String = "",
        val boardSize: BoardSize = BoardSize.Default,
    ) {
        val hasSmallerSize: Boolean get() = boardSize.previous() != null
        val hasBiggerSize: Boolean get() = boardSize.next() != null
    }

    sealed interface SideEffect

    interface Direction {
        fun openGame(size: BoardSize)
        fun openRecords(size: BoardSize)
        fun openSettings()
        fun openInfo()
    }
}
