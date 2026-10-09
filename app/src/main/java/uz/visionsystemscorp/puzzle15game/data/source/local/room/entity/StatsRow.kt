package uz.visionsystemscorp.puzzle15game.data.source.local.room.entity

/** Aggregate over game_results for one board size. */
data class StatsRow(
    val gamesPlayed: Int,
    val bestMoves: Int?,
    val bestSeconds: Long?,
    val averageMoves: Double?,
    val totalStars: Int?,
)
