package uz.visionsystemscorp.puzzle15game.domain.model

enum class AccountDeletion {
    DELETED,

    /** Firebase o'chirishdan oldin qayta kirishni talab qildi; yangi Google token bilan qayta urinish kerak. */
    NEEDS_REAUTH,
    FAILED,
}
