package uz.visionsystemscorp.puzzle15game.domain.usecase.splash

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.model.AppStart
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository

class SplashUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : SplashUseCase {

    override fun resolveStart(): AppStart {
        if (!profileRepository.isOnboardingDone()) return AppStart.ONBOARDING
        authRepository.restoreSession()
        return AppStart.MAIN
    }
}
