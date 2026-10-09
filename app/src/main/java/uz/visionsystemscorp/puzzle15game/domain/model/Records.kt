package uz.visionsystemscorp.puzzle15game.domain.model

data class LeaderboardEntry(
    val rank: Int?,
    val playerName: String,
    val moves: Int,
    val seconds: Long,
)

enum class LeaderboardStatus { LOADING, READY, OFFLINE }

/**
 * @property you o'yinchining o'z qatori: reytingda bo'lsa bulutdan, aks holda o'rinsiz lokal natijasi.
 * @property percentile o'yinchi ortda qoldirgan boshqa o'yinchilar ulushi (0–100); noma'lum bo'lsa null.
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
    val percentile: Int? = null,
)

data class BestResult(val moves: Int, val seconds: Long) {
    fun isBeatenBy(moves: Int, seconds: Long): Boolean =
        moves < this.moves || (moves == this.moves && seconds < this.seconds)
}
