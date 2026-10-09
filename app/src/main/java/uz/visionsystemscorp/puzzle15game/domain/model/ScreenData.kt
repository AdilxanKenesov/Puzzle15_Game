package uz.visionsystemscorp.puzzle15game.domain.model

data class MainData(
    val playerName: String,
    val boardSize: BoardSize,
)

data class SettingsData(
    val settings: UserSettings,
    val playerName: String,
    val isSignedIn: Boolean,
)

data class RecordsData(
    val leaderboard: Leaderboard,
    val personal: PersonalStats?,
)
