package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey

sealed interface NavCommand {
    data class Push(val route: NavKey) : NavCommand
    /** Replaces the top entry, but only while it is still [from] (when given). */
    data class Replace(val route: NavKey, val from: NavKey? = null) : NavCommand
    data class ReplaceAll(val route: NavKey) : NavCommand
    /** Pops the top entry, but only while it is still [from] (when given), so a double tap pops once. */
    data class Back(val from: NavKey? = null) : NavCommand

    /** Pops everything above the last [route] entry. */
    data class BackTo(val route: NavKey) : NavCommand
}
