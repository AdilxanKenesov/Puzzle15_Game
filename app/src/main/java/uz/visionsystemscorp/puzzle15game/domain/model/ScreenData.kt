package uz.visionsystemscorp.puzzle15game.domain.model

data class MainData(
    val playerName: String,
    val boardSize: BoardSize,
)

data class SettingsData(
    val settings: UserSettings,
    val playerName: String,
    /** Signed in with Google (a guest is not). */
    val isSignedIn: Boolean,
    /** Any account, guest included, that can be deleted. */
    val hasAccount: Boolean,
)

data class RecordsData(
    val leaderboard: Leaderboard,
    val personal: PersonalStats?,
)
