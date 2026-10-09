package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey

sealed interface NavCommand {
    data class Push(val route: NavKey) : NavCommand
    /** Yuqoridagi ekranni almashtiradi, lekin faqat u hali [from] bo'lsa (berilgan bo'lsa). */
    data class Replace(val route: NavKey, val from: NavKey? = null) : NavCommand
    data class ReplaceAll(val route: NavKey) : NavCommand
    /** Yuqoridagi ekranni yopadi, faqat u hali [from] bo'lsa; ikki marta bosish bitta ekranni yopadi. */
    data class Back(val from: NavKey? = null) : NavCommand

    data class BackTo(val route: NavKey) : NavCommand
}
