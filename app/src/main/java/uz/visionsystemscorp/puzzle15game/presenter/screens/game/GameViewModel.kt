package uz.visionsystemscorp.puzzle15game.presenter.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import uz.visionsystemscorp.puzzle15game.domain.usecase.game.GameUseCase
import uz.visionsystemscorp.puzzle15game.navigation.GameRoute
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameContract.GameEvent
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameContract.GameUiState

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted route: GameRoute,
    private val direction: GameContract.Direction,
    private val gameUseCase: GameUseCase,
) : ViewModel(), GameContract.GameViewModel {

    private val size = route.size

    override val container = orbitContainer<GameUiState, GameContract.SideEffect>(GameUiState(size = size))

    private var timerJob: Job? = null

    init {
        intent {
            val game = gameUseCase.start(size)
            // Davom ettirilgan o'yin pauza holatida ochiladi, ko'rinmay turib vaqt ketmasin.
            reduce { state.withGame(game).copy(isPaused = game.moves > 0) }
        }
    }

    override fun onEventDispatcher(event: GameEvent) {
        when (event) {
            is GameEvent.TileClicked -> onTileClicked(event.index)
            GameEvent.ShuffleClicked -> intent {
                // G'alaba ekrani ochilyapti; hozir aralashtirish behuda bo'ladi.
                if (state.isSolved) return@intent
                stopTimer()
                val game = gameUseCase.restart(size)
                reduce { state.withGame(game).copy(isPaused = false, round = state.round + 1) }
            }
            GameEvent.PauseClicked -> intent {
                if (state.isSolved) return@intent
                reduce { state.copy(isPaused = true) }
                saveProgress(state)
            }
            GameEvent.ResumeClicked -> intent {
                reduce { state.copy(isPaused = false) }
                if (state.isStarted) startTimer()
            }
            GameEvent.ScreenHidden -> intent {
                if (!state.isStarted || state.isSolved) return@intent
                reduce { state.copy(isPaused = true) }
                saveProgress(state)
            }
            GameEvent.HomeClicked -> direction.backToMain()
            GameEvent.SettingsClicked -> direction.openSettings()
        }
    }

    private fun onTileClicked(index: Int) = intent {
        var moved = false
        // Siljitish reduce ichida: tez ikki bosish hech qachon eskirgan taxta ustida ishlamaydi.
        reduce {
            val board = state.board?.takeIf { state.acceptsMoves }?.slide(index) ?: return@reduce state
            moved = true
            state.copy(board = board, moves = state.moves + 1)
        }
        if (!moved) return@intent
        gameUseCase.onTileMoved()
        if (state.isSolved) {
            stopTimer()
            val result = gameUseCase.finish(size, state.moves, state.elapsedSeconds)
            // Oxirgi tosh joyiga tushishini kutib, keyin taxtadan chiqamiz.
            delay(WIN_DELAY_MILLIS)
            direction.openWin(result)
        } else {
            startTimer()
            saveProgress(state)
        }
    }

    private suspend fun saveProgress(state: GameUiState) {
        val board = state.board ?: return
        gameUseCase.saveProgress(SavedGame(board, state.moves, state.elapsedSeconds))
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                intent {
                    reduce { if (state.isPaused || state.isSolved) state else state.copy(elapsedSeconds = state.elapsedSeconds + 1) }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun GameUiState.withGame(game: SavedGame) =
        copy(board = game.board, moves = game.moves, elapsedSeconds = game.seconds)

    @AssistedFactory
    interface Factory {
        fun create(route: GameRoute): GameViewModel
    }

    private companion object {
        const val WIN_DELAY_MILLIS = 700L
    }
}
