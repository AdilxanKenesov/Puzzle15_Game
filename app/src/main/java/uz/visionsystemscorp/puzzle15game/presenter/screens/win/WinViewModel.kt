package uz.visionsystemscorp.puzzle15game.presenter.screens.win

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.navigation.WinRoute

@HiltViewModel(assistedFactory = WinViewModel.Factory::class)
class WinViewModel @AssistedInject constructor(
    @Assisted route: WinRoute,
    private val direction: WinContract.Direction,
) : ViewModel(), WinContract.WinViewModel {

    override val container = orbitContainer<WinContract.WinUiState, WinContract.SideEffect>(
        WinContract.WinUiState(route.result),
    )

    private val size get() = container.stateFlow.value.result.size

    override fun onEventDispatcher(event: WinContract.WinEvent) {
        when (event) {
            WinContract.WinEvent.HomeClicked -> direction.backToMain()
            WinContract.WinEvent.ReplayClicked -> direction.replay(size)
            WinContract.WinEvent.RecordsClicked -> direction.openRecords(size)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: WinRoute): WinViewModel
    }
}
