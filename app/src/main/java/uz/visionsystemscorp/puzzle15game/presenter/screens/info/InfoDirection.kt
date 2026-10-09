package uz.visionsystemscorp.puzzle15game.presenter.screens.info

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator

class InfoDirection @Inject constructor(
    private val navigator: AppNavigator,
) : InfoContract.Direction {

    override fun back() = navigator.back()
}
