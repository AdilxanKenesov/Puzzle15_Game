package uz.visionsystemscorp.puzzle15game.domain.repository

interface FeedbackRepository {
    fun playClick()

    fun playMove()

    fun playWin()
}
