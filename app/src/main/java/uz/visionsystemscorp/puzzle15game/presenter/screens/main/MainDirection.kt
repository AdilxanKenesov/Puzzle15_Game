package uz.visionsystemscorp.puzzle15game.presenter.screens.main

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.InfoRoute
import uz.visionsystemscorp.puzzle15game.navigation.SettingsRoute
import uz.visionsystemscorp.puzzle15game.navigation.toGameRoute
import uz.visionsystemscorp.puzzle15game.navigation.toRecordsRoute

class MainDirection @Inject constructor(
    private val navigator: AppNavigator,
) : MainContract.Direction {

    override fun openGame(size: BoardSize) = navigator.navigateTo(size.toGameRoute())

    override fun openRecords(size: BoardSize) = navigator.navigateTo(size.toRecordsRoute())

    override fun openSettings() = navigator.navigateTo(SettingsRoute)

    override fun openInfo() = navigator.navigateTo(InfoRoute)
}
