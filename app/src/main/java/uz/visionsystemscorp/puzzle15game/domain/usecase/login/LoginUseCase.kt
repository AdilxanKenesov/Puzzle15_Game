package uz.visionsystemscorp.puzzle15game.domain.usecase.login

interface LoginUseCase {
    /** Starts a guest session. Playing on works even if this fails (e.g. offline); it is retried on next launch. */
    suspend fun signInAsGuest()

    /** @return whether the player already has a name, e.g. one restored from their Google account. */
    suspend fun signInWithGoogle(idToken: String): Result<Boolean>

    fun completeOnboarding()
}
