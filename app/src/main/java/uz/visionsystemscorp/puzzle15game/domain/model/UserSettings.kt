package uz.visionsystemscorp.puzzle15game.domain.model

data class UserSettings(
    val music: Boolean = true,
    val sound: Boolean = true,
    val vibration: Boolean = true,
    val boardSize: BoardSize = BoardSize.Default,
)
