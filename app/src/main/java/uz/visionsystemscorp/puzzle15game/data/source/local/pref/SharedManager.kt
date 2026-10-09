package uz.visionsystemscorp.puzzle15game.data.source.local.pref

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedManager @Inject constructor(
    private val prefs: SharedPreferences,
) {

    var onboardingDone: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = prefs.edit { putBoolean(KEY_ONBOARDING_DONE, value) }

    var playerName: String
        get() = prefs.getString(KEY_PLAYER_NAME, null).orEmpty()
        set(value) = prefs.edit { putString(KEY_PLAYER_NAME, value) }

    var music: Boolean
        get() = prefs.getBoolean(KEY_MUSIC, true)
        set(value) = prefs.edit { putBoolean(KEY_MUSIC, value) }

    var sound: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit { putBoolean(KEY_SOUND, value) }

    var vibration: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit { putBoolean(KEY_VIBRATION, value) }

    var boardDimension: Int
        get() = prefs.getInt(KEY_BOARD_DIMENSION, 0)
        set(value) = prefs.edit { putInt(KEY_BOARD_DIMENSION, value) }

    /** O'yinchi o'zi chiqib ketgan bo'lsa true: unga avtomatik mehmon sessiyasi ochilmaydi. */
    var signedOut: Boolean
        get() = prefs.getBoolean(KEY_SIGNED_OUT, false)
        set(value) = prefs.edit { putBoolean(KEY_SIGNED_OUT, value) }

    /** Har bir o'lcham uchun serverga oxirgi tasdiqlangan yozuv ("uid|ism|yurish|soniya"), takroriy yozuvni oldini oladi. */
    fun publishedBest(dimension: Int): String? = prefs.getString(KEY_PUBLISHED_BEST + dimension, null)

    fun setPublishedBest(dimension: Int, signature: String) =
        prefs.edit { putString(KEY_PUBLISHED_BEST + dimension, signature) }

    companion object {
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
        private const val KEY_PLAYER_NAME = "player_name"
        private const val KEY_MUSIC = "music"
        private const val KEY_SOUND = "sound"
        private const val KEY_VIBRATION = "vibration"
        private const val KEY_BOARD_DIMENSION = "board_dimension"
        private const val KEY_SIGNED_OUT = "signed_out"
        private const val KEY_PUBLISHED_BEST = "published_best_"
    }
}
