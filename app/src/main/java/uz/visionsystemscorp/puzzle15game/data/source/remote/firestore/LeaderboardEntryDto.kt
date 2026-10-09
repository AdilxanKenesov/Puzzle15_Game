package uz.visionsystemscorp.puzzle15game.data.source.remote.firestore

data class LeaderboardEntryDto(
    val name: String,
    val moves: Int,
    val seconds: Long,
)
