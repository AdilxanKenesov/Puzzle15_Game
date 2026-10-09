package uz.visionsystemscorp.puzzle15game.data.repository_impl

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.visionsystemscorp.puzzle15game.data.source.local.sound.HapticManager
import uz.visionsystemscorp.puzzle15game.data.source.local.sound.SoundManager
import uz.visionsystemscorp.puzzle15game.di.ApplicationScope
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.SettingsRepository

/**
 * Plays effects according to the sound / vibration settings and keeps background music
 * running only while music is enabled and the app is in the foreground.
 */
@Singleton
class FeedbackRepositoryImpl @Inject constructor(
    private val soundManager: SoundManager,
    private val hapticManager: HapticManager,
    private val settingsRepository: SettingsRepository,
    @ApplicationScope scope: CoroutineScope,
) : FeedbackRepository {

    private val inForeground = MutableStateFlow(false)

    init {
        scope.launch(Dispatchers.Main) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(
                object : DefaultLifecycleObserver {
                    override fun onStart(owner: LifecycleOwner) {
                        inForeground.value = true
                    }

                    override fun onStop(owner: LifecycleOwner) {
                        inForeground.value = false
                    }
                },
            )
            combine(settingsRepository.settings.map { it.music }, inForeground) { music, foreground -> music && foreground }
                .distinctUntilChanged()
                .collect { play -> if (play) soundManager.startMusic() else soundManager.pauseMusic() }
        }
    }

    override fun playClick() {
        if (settings.sound) soundManager.playClick()
    }

    override fun playMove() {
        if (settings.sound) soundManager.playSlide()
        if (settings.vibration) hapticManager.tick()
    }

    override fun playWin() {
        if (settings.sound) soundManager.playWin()
        if (settings.vibration) hapticManager.celebrate()
    }

    private val settings get() = settingsRepository.settings.value
}
