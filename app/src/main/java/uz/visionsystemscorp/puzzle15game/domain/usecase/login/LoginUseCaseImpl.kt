package uz.visionsystemscorp.puzzle15game.domain.usecase.login

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository

class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) : LoginUseCase {

    override suspend fun signInAsGuest() {
        authRepository.signInAsGuest()
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Boolean> =
        authRepository.signInWithGoogle(idToken).map {
            profileRepository.syncName()
            PlayerProfile.isValidName(profileRepository.profile.value.name)
        }

    override fun completeOnboarding() = profileRepository.setOnboardingDone()
}
