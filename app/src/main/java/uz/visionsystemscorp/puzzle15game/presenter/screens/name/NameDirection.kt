package uz.visionsystemscorp.puzzle15game.presenter.screens.name

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.NameRoute
import uz.visionsystemscorp.puzzle15game.navigation.MainRoute

class NameDirection @Inject constructor(
    private val navigator: AppNavigator,
) : NameContract.Direction {

    override fun openMain() = navigator.replaceAll(MainRoute)

    override fun back() = navigator.back(from = NameRoute(isOnboarding = false))
}
