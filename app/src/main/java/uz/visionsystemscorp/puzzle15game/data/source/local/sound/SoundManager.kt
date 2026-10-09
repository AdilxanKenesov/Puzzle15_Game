package uz.visionsystemscorp.puzzle15game.data.source.local.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import uz.visionsystemscorp.puzzle15game.R

/** SoundPool (qisqa effektlar) va MediaPlayer (takrorlanuvchi musiqa). Faqat main thread'dan chaqiriladi. */
@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val gameAudio = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(gameAudio)
        .build()

    private val clickId = soundPool.load(context, R.raw.sfx_click, 1)
    private val slideId = soundPool.load(context, R.raw.sfx_slide, 1)
    private val winId = soundPool.load(context, R.raw.sfx_win, 1)

    private var music: MediaPlayer? = null

    fun playClick() = play(clickId, volume = 0.7f)

    fun playSlide() = play(slideId, volume = 0.9f)

    fun playWin() = play(winId, volume = 1f)

    fun startMusic() {
        val player = music ?: MediaPlayer.create(context, R.raw.music_jungle)?.apply {
            isLooping = true
            setVolume(MUSIC_VOLUME, MUSIC_VOLUME)
        }.also { music = it }
        if (player != null && !player.isPlaying) player.start()
    }

    fun pauseMusic() {
        music?.takeIf { it.isPlaying }?.pause()
    }

    private fun play(soundId: Int, volume: Float) {
        soundPool.play(soundId, volume, volume, 1, 0, 1f)
    }

    private companion object {
        const val MUSIC_VOLUME = 0.45f
    }
}
