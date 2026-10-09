package uz.visionsystemscorp.puzzle15game.domain.usecase.login

interface LoginUseCase {
    /** Mehmon sessiyasini ochadi. Ochilmasa ham (masalan, internetsiz) o'yin davom etadi, keyingi ochilishda qayta uriniladi. */
    suspend fun signInAsGuest()

    /** @return o'yinchining ismi bormi (masalan, Google akkauntidan tiklangan). */
    suspend fun signInWithGoogle(idToken: String): Result<Boolean>

    fun completeOnboarding()
}
