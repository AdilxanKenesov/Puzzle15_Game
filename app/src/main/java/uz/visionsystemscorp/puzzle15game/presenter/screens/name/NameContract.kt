package uz.visionsystemscorp.puzzle15game.presenter.screens.name

import org.orbitmvi.orbit.OrbitContainerHost
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile

interface NameContract {

    interface NameViewModel : OrbitContainerHost<NameUiState, NameUiState, SideEffect> {
        fun onEventDispatcher(event: NameEvent)
    }

    sealed interface NameEvent {
        data class NameChanged(val value: String) : NameEvent
        data object SaveClicked : NameEvent
    }

    data class NameUiState(
        val name: String = "",
        val isOnboarding: Boolean = true,
    ) {
        val canSave: Boolean get() = PlayerProfile.isValidName(name)
    }

    sealed interface SideEffect

    interface Direction {
        fun openMain()
        fun back()
    }
}
