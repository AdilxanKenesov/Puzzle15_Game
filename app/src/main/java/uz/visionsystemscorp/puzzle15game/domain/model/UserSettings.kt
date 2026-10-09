package uz.visionsystemscorp.puzzle15game.domain.model

data class UserSettings(
    val music: Boolean = true,
    val sound: Boolean = true,
    val vibration: Boolean = true,
    /** Board size last picked on the Main screen. */
    val boardSize: BoardSize = BoardSize.Default,
) {
    /** The speaker button on Main / Game mutes or unmutes music and effects together. */
    val isAudioOn: Boolean get() = music || sound

    fun withAudio(on: Boolean): UserSettings = copy(music = on, sound = on)
}
