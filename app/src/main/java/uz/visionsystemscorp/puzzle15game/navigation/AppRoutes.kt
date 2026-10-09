package uz.visionsystemscorp.puzzle15game.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult

@Serializable
data object SplashRoute : NavKey

/** @property isOnboarding true on first launch; false when opened from Settings. */
@Serializable
data class LoginRoute(val isOnboarding: Boolean) : NavKey

/** @property isOnboarding true on first launch; false when renaming from Settings. */
@Serializable
data class NameRoute(val isOnboarding: Boolean) : NavKey

@Serializable
data object MainRoute : NavKey

@Serializable
data class GameRoute(val boardDimension: Int) : NavKey {
    val size: BoardSize get() = BoardSize.fromDimension(boardDimension)
}

@Serializable
data class WinRoute(
    val boardDimension: Int,
    val moves: Int,
    val seconds: Long,
    val stars: Int,
    val isNewBest: Boolean,
) : NavKey {
    val result: GameResult get() = GameResult(BoardSize.fromDimension(boardDimension), moves, seconds, stars, isNewBest)
}

@Serializable
data class RecordsRoute(val boardDimension: Int) : NavKey {
    val size: BoardSize get() = BoardSize.fromDimension(boardDimension)
}

@Serializable
data object SettingsRoute : NavKey

@Serializable
data object InfoRoute : NavKey

fun BoardSize.toGameRoute() = GameRoute(dimension)

fun BoardSize.toRecordsRoute() = RecordsRoute(dimension)

fun GameResult.toWinRoute() = WinRoute(size.dimension, moves, seconds, stars, isNewBest)
