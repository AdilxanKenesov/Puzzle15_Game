package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey

interface AppNavigator {
    fun navigateTo(route: NavKey)
    fun replaceTo(route: NavKey, from: NavKey? = null)
    fun replaceAll(route: NavKey)

    fun back(from: NavKey? = null)
    fun backTo(route: NavKey)
}
