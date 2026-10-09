package uz.visionsystemscorp.puzzle15game.data.source.remote.firestore

/** One player's best result on one board size, as stored in `leaderboards/{size}/entries/{uid}`. */
data class LeaderboardEntryDto(
    val name: String,
    val moves: Int,
    val seconds: Long,
)
