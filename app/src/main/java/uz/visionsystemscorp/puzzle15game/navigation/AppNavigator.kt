package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey

interface AppNavigator {
    fun navigateTo(route: NavKey)
    /** @param from the screen asking; ignored if the user has already left it. */
    fun replaceTo(route: NavKey, from: NavKey? = null)
    fun replaceAll(route: NavKey)

    /** @param from the screen asking; ignored if it is no longer on top (e.g. a double tap). */
    fun back(from: NavKey? = null)
    fun backTo(route: NavKey)
}
