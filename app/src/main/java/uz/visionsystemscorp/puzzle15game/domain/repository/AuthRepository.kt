package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uz.visionsystemscorp.puzzle15game.domain.model.Account
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion

interface AuthRepository {
    val account: StateFlow<Account?>

    suspend fun signInAsGuest(): Result<Account>

    /**
     * Mehmonni [idToken] dagi Google akkauntga ulaydi, uid o'zgarmaydi. Bu Google akkaunt allaqachon
     * ishlatilgan bo'lsa, mehmonning bulutdagi ma'lumoti o'chiriladi va sessiya o'sha akkauntga o'tadi.
     */
    suspend fun signInWithGoogle(idToken: String): Result<Account>

    suspend fun signOut()

    /**
     * Akkauntni va uning bulutdagi barcha ma'lumotini o'chiradi, keyin tizimdan chiqaradi.
     * @param googleIdToken [AccountDeletion.NEEDS_REAUTH] dan keyin olingan yangi token.
     */
    suspend fun deleteAccount(googleIdToken: String? = null): AccountDeletion

    /** Mehmon sessiyasi yo'qolgan bo'lsa (masalan, birinchi kirish internetsiz bo'lgan), uni fonda qayta ochadi. */
    fun restoreSession()
}
