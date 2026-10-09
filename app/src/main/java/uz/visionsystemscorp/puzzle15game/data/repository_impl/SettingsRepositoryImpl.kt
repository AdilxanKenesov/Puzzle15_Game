package uz.visionsystemscorp.puzzle15game.data.repository_impl

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import uz.visionsystemscorp.puzzle15game.data.source.local.pref.SharedManager
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.UserSettings
import uz.visionsystemscorp.puzzle15game.domain.repository.SettingsRepository

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val sharedManager: SharedManager,
) : SettingsRepository {

    private val state = MutableStateFlow(
        UserSettings(
            music = sharedManager.music,
            sound = sharedManager.sound,
            vibration = sharedManager.vibration,
            boardSize = BoardSize.fromDimension(sharedManager.boardDimension),
        ),
    )

    override val settings: StateFlow<UserSettings> = state.asStateFlow()

    override fun update(transform: (UserSettings) -> UserSettings) {
        val updated = state.updateAndGet(transform)
        sharedManager.music = updated.music
        sharedManager.sound = updated.sound
        sharedManager.vibration = updated.vibration
        sharedManager.boardDimension = updated.boardSize.dimension
    }
}
