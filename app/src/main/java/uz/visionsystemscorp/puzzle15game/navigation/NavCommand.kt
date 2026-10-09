package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey

sealed interface NavCommand {
    data class Push(val route: NavKey) : NavCommand
    data class Replace(val route: NavKey) : NavCommand
    data class ReplaceAll(val route: NavKey) : NavCommand
    data object Back : NavCommand

    /** Pops everything above the last [route] entry. */
    data class BackTo(val route: NavKey) : NavCommand
}
