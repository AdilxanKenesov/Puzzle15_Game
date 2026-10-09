package uz.visionsystemscorp.puzzle15game.presenter.screens.splash

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.LoginRoute
import uz.visionsystemscorp.puzzle15game.navigation.MainRoute

class SplashDirection @Inject constructor(
    private val navigator: AppNavigator,
) : SplashContract.Direction {

    override fun openOnboarding() = navigator.replaceAll(LoginRoute(isOnboarding = true))

    override fun openMain() = navigator.replaceAll(MainRoute)
}
