package uz.visionsystemscorp.puzzle15game.domain.model

/** The signed-in player. A guest is an anonymous account that lives only on this device. */
data class Account(
    val uid: String,
    val isGuest: Boolean,
)
