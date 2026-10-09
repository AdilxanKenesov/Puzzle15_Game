package uz.visionsystemscorp.puzzle15game.presenter.screens.game

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.MainRoute
import uz.visionsystemscorp.puzzle15game.navigation.SettingsRoute
import uz.visionsystemscorp.puzzle15game.navigation.toGameRoute
import uz.visionsystemscorp.puzzle15game.navigation.toWinRoute

class GameDirection @Inject constructor(
    private val navigator: AppNavigator,
) : GameContract.Direction {

    /** Win replaces the game so Back from Win returns to Main. */
    override fun openWin(result: GameResult) = navigator.replaceTo(result.toWinRoute(), from = result.size.toGameRoute())

    override fun openSettings() = navigator.navigateTo(SettingsRoute)

    override fun backToMain() = navigator.backTo(MainRoute)
}
