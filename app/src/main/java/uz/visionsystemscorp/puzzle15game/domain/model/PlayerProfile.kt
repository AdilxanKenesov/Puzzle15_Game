package uz.visionsystemscorp.puzzle15game.domain.model

data class PlayerProfile(
    val name: String = "",
) {
    companion object {
        const val NAME_MIN_LENGTH = 3
        const val NAME_MAX_LENGTH = 16

        fun isValidName(name: String): Boolean = name.trim().length in NAME_MIN_LENGTH..NAME_MAX_LENGTH
    }
}
