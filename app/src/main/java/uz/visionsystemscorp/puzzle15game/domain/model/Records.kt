package uz.visionsystemscorp.puzzle15game.domain.model

/** @property rank null while the player's global position is unknown. */
data class LeaderboardEntry(
    val rank: Int?,
    val playerName: String,
    val moves: Int,
    val seconds: Long,
)

enum class LeaderboardStatus { LOADING, READY, OFFLINE }

/**
 * @property you the player's own row; from the cloud when ranked, otherwise their local best with no rank.
 * @property percentile share of other players this player beats, 0–100; null when unknown.
 */
data class Leaderboard(
    val entries: List<LeaderboardEntry> = emptyList(),
    val you: LeaderboardEntry? = null,
    val percentile: Int? = null,
    val status: LeaderboardStatus = LeaderboardStatus.LOADING,
)

data class PersonalStats(
    val bestMoves: Int,
    val bestSeconds: Long,
    val gamesPlayed: Int,
    val averageMoves: Int,
    val totalStars: Int,
    /** Share of other players this player beats, 0–100; null until world data is available. */
    val percentile: Int? = null,
)

/** Best finished game on one board size. */
data class BestResult(val moves: Int, val seconds: Long) {
    fun isBeatenBy(moves: Int, seconds: Long): Boolean =
        moves < this.moves || (moves == this.moves && seconds < this.seconds)
}
