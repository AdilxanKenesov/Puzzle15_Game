package uz.visionsystemscorp.puzzle15game.presenter.screens.records

import org.orbitmvi.orbit.OrbitContainerHost
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats

interface RecordsContract {

    interface RecordsViewModel : OrbitContainerHost<RecordsUiState, RecordsUiState, SideEffect> {
        fun onEventDispatcher(event: RecordsEvent)
    }

    sealed interface RecordsEvent {
        data class SizeSelected(val size: BoardSize) : RecordsEvent
        data class TabSelected(val tab: RecordsTab) : RecordsEvent
        data object HomeClicked : RecordsEvent
        data object PlayClicked : RecordsEvent
    }

    enum class RecordsTab { World, Mine }

    data class RecordsUiState(
        val size: BoardSize = BoardSize.Default,
        val tab: RecordsTab = RecordsTab.World,
        val leaderboard: Leaderboard = Leaderboard(),
        val personal: PersonalStats? = null,
    )

    sealed interface SideEffect

    interface Direction {
        fun backToMain()
        fun openGame(size: BoardSize)
    }
}
