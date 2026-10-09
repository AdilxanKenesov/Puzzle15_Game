package uz.visionsystemscorp.puzzle15game.presenter.screens.login

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.MainRoute
import uz.visionsystemscorp.puzzle15game.navigation.NameRoute

class LoginDirection @Inject constructor(
    private val navigator: AppNavigator,
) : LoginContract.Direction {

    override fun openName() = navigator.navigateTo(NameRoute(isOnboarding = true))

    override fun openMain() = navigator.replaceAll(MainRoute)

    override fun back() = navigator.back()
}
