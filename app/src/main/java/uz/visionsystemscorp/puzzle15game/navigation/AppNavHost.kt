package uz.visionsystemscorp.puzzle15game.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.info.InfoScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.main.MainScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.name.NameScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.splash.SplashScreen
import uz.visionsystemscorp.puzzle15game.presenter.screens.win.WinScreen

/**
 * Splash → (first launch) Login → Name → Main; Main → Game → Win; Main → Records / Settings / Info.
 * ViewModels navigate through [AppNavigator]; this host applies their commands to the back stack.
 */
@Composable
fun AppNavHost(handler: AppNavigationHandler) {
    val backStack = rememberNavBackStack(SplashRoute)

    LaunchedEffect(handler) {
        handler.commands.collect { command ->
            when (command) {
                // Ignore a repeated push from a double tap.
                is NavCommand.Push -> if (backStack.lastOrNull() != command.route) backStack.add(command.route)
                is NavCommand.Replace -> backStack[backStack.lastIndex] = command.route
                is NavCommand.ReplaceAll -> backStack.resetTo(command.route)
                NavCommand.Back -> backStack.pop()
                is NavCommand.BackTo -> {
                    if (command.route in backStack) {
                        while (backStack.last() != command.route) backStack.removeAt(backStack.lastIndex)
                    } else {
                        backStack.resetTo(command.route)
                    }
                }
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.pop() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<SplashRoute> { SplashScreen() }
            entry<LoginRoute> { LoginScreen(it) }
            entry<NameRoute> { NameScreen(it) }
            entry<MainRoute> { MainScreen() }
            entry<GameRoute> { GameScreen(it) }
            entry<WinRoute> { WinScreen(it) }
            entry<RecordsRoute> { RecordsScreen(it) }
            entry<SettingsRoute> { SettingsScreen() }
            entry<InfoRoute> { InfoScreen() }
        },
    )
}

private fun NavBackStack<NavKey>.pop() {
    if (size > 1) removeAt(lastIndex)
}

private fun NavBackStack<NavKey>.resetTo(route: NavKey) {
    add(route)
    while (size > 1) removeAt(0)
}
