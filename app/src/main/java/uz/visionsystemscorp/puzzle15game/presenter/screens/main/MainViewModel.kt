package uz.visionsystemscorp.puzzle15game.presenter.screens.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.usecase.main.MainUseCase

@HiltViewModel
class MainViewModel @Inject constructor(
    private val direction: MainContract.Direction,
    private val mainUseCase: MainUseCase,
) : ViewModel(), MainContract.MainViewModel {

    override val container = orbitContainer<MainContract.MainUiState, MainContract.SideEffect>(
        MainContract.MainUiState(),
    )

    init {
        intent {
            mainUseCase.observe().collect { data ->
                reduce { state.copy(playerName = data.playerName, boardSize = data.boardSize) }
            }
        }
    }

    override fun onEventDispatcher(event: MainContract.MainEvent) {
        when (event) {
            MainContract.MainEvent.PreviousSizeClicked -> mainUseCase.selectPreviousSize()
            MainContract.MainEvent.NextSizeClicked -> mainUseCase.selectNextSize()
            MainContract.MainEvent.PlayClicked -> direction.openGame(container.stateFlow.value.boardSize)
            MainContract.MainEvent.RecordsClicked -> direction.openRecords(container.stateFlow.value.boardSize)
            MainContract.MainEvent.SettingsClicked -> direction.openSettings()
            MainContract.MainEvent.InfoClicked -> direction.openInfo()
        }
    }
}
