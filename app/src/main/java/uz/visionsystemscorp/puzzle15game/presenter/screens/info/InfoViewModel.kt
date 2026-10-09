package uz.visionsystemscorp.puzzle15game.presenter.screens.info

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.viewmodel.orbitContainer

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val direction: InfoContract.Direction,
) : ViewModel(), InfoContract.InfoViewModel {

    override val container = orbitContainer<InfoContract.InfoUiState, InfoContract.SideEffect>(InfoContract.InfoUiState)

    override fun onEventDispatcher(event: InfoContract.InfoEvent) {
        when (event) {
            InfoContract.InfoEvent.CloseClicked -> direction.back()
        }
    }
}
