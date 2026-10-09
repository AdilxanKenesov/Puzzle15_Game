package uz.visionsystemscorp.puzzle15game.data.source.local.room.entity

data class StatsRow(
    val gamesPlayed: Int,
    val bestMoves: Int?,
    val bestSeconds: Long?,
    val averageMoves: Double?,
    val totalStars: Int?,
)
