package uz.visionsystemscorp.puzzle15game.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigationHandler {
    val commands: Flow<NavCommand>
}
