package uz.visionsystemscorp.puzzle15game.presenter.screens.win

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.MainRoute
import uz.visionsystemscorp.puzzle15game.navigation.toGameRoute
import uz.visionsystemscorp.puzzle15game.navigation.toRecordsRoute
import uz.visionsystemscorp.puzzle15game.navigation.toWinRoute

class WinDirection @Inject constructor(
    private val navigator: AppNavigator,
) : WinContract.Direction {

    override fun backToMain() = navigator.backTo(MainRoute)

    override fun replay(result: GameResult) = navigator.replaceTo(result.size.toGameRoute(), from = result.toWinRoute())

    override fun openRecords(size: BoardSize) = navigator.navigateTo(size.toRecordsRoute())
}
