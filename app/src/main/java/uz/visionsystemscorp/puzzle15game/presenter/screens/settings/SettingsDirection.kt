package uz.visionsystemscorp.puzzle15game.presenter.screens.settings

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.SettingsRoute
import uz.visionsystemscorp.puzzle15game.navigation.LoginRoute
import uz.visionsystemscorp.puzzle15game.navigation.NameRoute

class SettingsDirection @Inject constructor(
    private val navigator: AppNavigator,
) : SettingsContract.Direction {

    override fun openNameEditor() = navigator.navigateTo(NameRoute(isOnboarding = false))

    override fun openLogin() = navigator.navigateTo(LoginRoute(isOnboarding = false))

    override fun back() = navigator.back(from = SettingsRoute)

    override fun restartOnboarding() = navigator.replaceAll(LoginRoute(isOnboarding = true))
}
