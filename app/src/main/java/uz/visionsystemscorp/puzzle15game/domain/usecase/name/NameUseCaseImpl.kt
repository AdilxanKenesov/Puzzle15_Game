package uz.visionsystemscorp.puzzle15game.domain.usecase.name

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository

class NameUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : NameUseCase {

    override fun currentName(): String = profileRepository.profile.value.name

    override fun saveName(name: String, completeOnboarding: Boolean) {
        profileRepository.setName(name.trim())
        if (completeOnboarding) profileRepository.setOnboardingDone()
    }
}
