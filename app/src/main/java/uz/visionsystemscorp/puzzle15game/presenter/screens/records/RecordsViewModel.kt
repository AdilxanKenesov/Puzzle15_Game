package uz.visionsystemscorp.puzzle15game.presenter.screens.records

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.usecase.records.RecordsUseCase
import uz.visionsystemscorp.puzzle15game.navigation.RecordsRoute
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsContract.RecordsEvent

@HiltViewModel(assistedFactory = RecordsViewModel.Factory::class)
class RecordsViewModel @AssistedInject constructor(
    @Assisted route: RecordsRoute,
    private val direction: RecordsContract.Direction,
    private val recordsUseCase: RecordsUseCase,
) : ViewModel(), RecordsContract.RecordsViewModel {

    override val container = orbitContainer<RecordsContract.RecordsUiState, RecordsContract.SideEffect>(
        RecordsContract.RecordsUiState(size = route.size),
    )

    private val selectedSize = MutableStateFlow(route.size)

    init {
        @OptIn(ExperimentalCoroutinesApi::class)
        intent {
            selectedSize.flatMapLatest(recordsUseCase::observe).collect { data ->
                reduce { state.copy(leaderboard = data.leaderboard, personal = data.personal) }
            }
        }
    }

    override fun onEventDispatcher(event: RecordsEvent) {
        when (event) {
            is RecordsEvent.SizeSelected -> intent {
                selectedSize.value = event.size
                reduce { state.copy(size = event.size) }
            }
            is RecordsEvent.TabSelected -> intent { reduce { state.copy(tab = event.tab) } }
            RecordsEvent.HomeClicked -> direction.backToMain()
            RecordsEvent.PlayClicked -> direction.openGame(container.stateFlow.value.size)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: RecordsRoute): RecordsViewModel
    }
}
