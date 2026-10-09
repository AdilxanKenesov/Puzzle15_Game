package uz.visionsystemscorp.puzzle15game.domain.repository

/**
 * Sound effects and haptics. Background music follows the music setting on its own,
 * so callers only trigger one-shot feedback.
 */
interface FeedbackRepository {
    fun playClick()

    fun playMove()

    fun playWin()
}
