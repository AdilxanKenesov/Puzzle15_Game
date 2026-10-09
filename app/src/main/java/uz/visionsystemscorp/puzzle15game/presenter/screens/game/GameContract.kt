package uz.visionsystemscorp.puzzle15game.presenter.screens.game

import org.orbitmvi.orbit.OrbitContainerHost
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult

interface GameContract {

    interface GameViewModel : OrbitContainerHost<GameUiState, GameUiState, SideEffect> {
        fun onEventDispatcher(event: GameEvent)
    }

    sealed interface GameEvent {
        data class TileClicked(val index: Int) : GameEvent
        data object ShuffleClicked : GameEvent
        data object PauseClicked : GameEvent
        data object ResumeClicked : GameEvent
        data object AudioClicked : GameEvent
        data object HomeClicked : GameEvent
        data object SettingsClicked : GameEvent

        /** The board left the screen or the app went to background. */
        data object ScreenHidden : GameEvent
    }

    data class GameUiState(
        val size: BoardSize = BoardSize.Default,
        /** Null while the saved game is loading. */
        val board: Board? = null,
        val moves: Int = 0,
        val elapsedSeconds: Long = 0,
        val isPaused: Boolean = false,
        val isAudioOn: Boolean = true,
        /** Bumped on every new shuffle so the tiles replay their entrance animation. */
        val round: Int = 0,
    ) {
        val isStarted: Boolean get() = moves > 0
        val isSolved: Boolean get() = board?.isSolved == true
        val acceptsMoves: Boolean get() = board != null && !isPaused && !isSolved
    }

    sealed interface SideEffect

    interface Direction {
        fun openWin(result: GameResult)
        fun openSettings()
        fun backToMain()
    }
}
