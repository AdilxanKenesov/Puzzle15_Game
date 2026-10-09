package uz.visionsystemscorp.puzzle15game.domain.model

enum class AccountDeletion {
    DELETED,

    /** Firebase wants a fresh sign-in before deleting; retry with a new Google ID token. */
    NEEDS_REAUTH,
    FAILED,
}
