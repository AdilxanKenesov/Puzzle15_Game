package uz.visionsystemscorp.puzzle15game.domain.model

data class GameResult(
    val size: BoardSize,
    val moves: Int,
    val seconds: Long,
    val stars: Int,
    val isNewBest: Boolean,
)
