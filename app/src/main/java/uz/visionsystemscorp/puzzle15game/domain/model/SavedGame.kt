package uz.visionsystemscorp.puzzle15game.domain.model

/** An unfinished game kept per board size so the player can continue later. */
data class SavedGame(
    val board: Board,
    val moves: Int,
    val seconds: Long,
)
