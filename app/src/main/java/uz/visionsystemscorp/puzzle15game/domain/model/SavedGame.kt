package uz.visionsystemscorp.puzzle15game.domain.model

data class SavedGame(
    val board: Board,
    val moves: Int,
    val seconds: Long,
)
