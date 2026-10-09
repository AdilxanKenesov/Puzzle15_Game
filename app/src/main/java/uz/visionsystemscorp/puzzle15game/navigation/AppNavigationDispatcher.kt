package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Singleton
class AppNavigationDispatcher @Inject constructor() : AppNavigator, AppNavigationHandler {

    private val channel = Channel<NavCommand>(Channel.BUFFERED)

    override val commands: Flow<NavCommand> = channel.receiveAsFlow()

    private fun navigate(command: NavCommand) {
        channel.trySend(command)
    }

    override fun navigateTo(route: NavKey) = navigate(NavCommand.Push(route))

    override fun replaceTo(route: NavKey, from: NavKey?) = navigate(NavCommand.Replace(route, from))

    override fun replaceAll(route: NavKey) = navigate(NavCommand.ReplaceAll(route))

    override fun back(from: NavKey?) = navigate(NavCommand.Back(from))

    override fun backTo(route: NavKey) = navigate(NavCommand.BackTo(route))
}
