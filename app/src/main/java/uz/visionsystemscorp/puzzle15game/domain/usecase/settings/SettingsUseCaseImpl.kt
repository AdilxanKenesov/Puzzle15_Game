package uz.visionsystemscorp.puzzle15game.domain.usecase.settings

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion
import uz.visionsystemscorp.puzzle15game.domain.model.SettingsData
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.GameRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.SettingsRepository

class SettingsUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val recordsRepository: RecordsRepository,
    private val gameRepository: GameRepository,
) : SettingsUseCase {

    override fun observe(): Flow<SettingsData> =
        combine(profileRepository.profile, settingsRepository.settings, authRepository.account) { profile, settings, account ->
            // A guest can still sign in with Google to keep their records.
            SettingsData(
                settings = settings,
                playerName = profile.name,
                isSignedIn = account != null && !account.isGuest,
                hasAccount = account != null,
            )
        }

    override fun setMusic(on: Boolean) = settingsRepository.update { it.copy(music = on) }

    override fun setSound(on: Boolean) = settingsRepository.update { it.copy(sound = on) }

    override fun setVibration(on: Boolean) = settingsRepository.update { it.copy(vibration = on) }

    override suspend fun signOut() = authRepository.signOut()

    override suspend fun deleteAccount(googleIdToken: String?): AccountDeletion =
        authRepository.deleteAccount(googleIdToken).also { result ->
            if (result == AccountDeletion.DELETED) {
                recordsRepository.clearAll()
                gameRepository.clearAll()
                profileRepository.reset()
            }
        }
}
