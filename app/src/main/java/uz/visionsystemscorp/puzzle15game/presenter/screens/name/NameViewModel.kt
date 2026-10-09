package uz.visionsystemscorp.puzzle15game.presenter.screens.name

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.domain.usecase.name.NameUseCase
import uz.visionsystemscorp.puzzle15game.navigation.NameRoute

@HiltViewModel(assistedFactory = NameViewModel.Factory::class)
class NameViewModel @AssistedInject constructor(
    @Assisted route: NameRoute,
    private val direction: NameContract.Direction,
    private val nameUseCase: NameUseCase,
) : ViewModel(), NameContract.NameViewModel {

    override val container = orbitContainer<NameContract.NameUiState, NameContract.SideEffect>(
        NameContract.NameUiState(name = nameUseCase.currentName(), isOnboarding = route.isOnboarding),
    )

    override fun onEventDispatcher(event: NameContract.NameEvent) {
        when (event) {
            is NameContract.NameEvent.NameChanged -> intent {
                if (event.value.length <= PlayerProfile.NAME_MAX_LENGTH) reduce { state.copy(name = event.value) }
            }

            NameContract.NameEvent.SaveClicked -> intent {
                if (!state.canSave) return@intent
                nameUseCase.saveName(state.name, completeOnboarding = state.isOnboarding)
                if (state.isOnboarding) direction.openMain() else direction.back()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: NameRoute): NameViewModel
    }
}
