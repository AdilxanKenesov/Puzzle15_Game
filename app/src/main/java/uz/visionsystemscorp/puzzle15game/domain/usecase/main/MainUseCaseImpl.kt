package uz.visionsystemscorp.puzzle15game.domain.usecase.main

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uz.visionsystemscorp.puzzle15game.domain.model.MainData
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.SettingsRepository

class MainUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
) : MainUseCase {

    override fun observe(): Flow<MainData> =
        combine(profileRepository.profile, settingsRepository.settings) { profile, settings ->
            MainData(profile.name, settings.boardSize)
        }

    override fun selectPreviousSize() = settingsRepository.update { it.copy(boardSize = it.boardSize.previous() ?: it.boardSize) }

    override fun selectNextSize() = settingsRepository.update { it.copy(boardSize = it.boardSize.next() ?: it.boardSize) }
}
