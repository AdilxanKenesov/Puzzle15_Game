package uz.visionsystemscorp.puzzle15game.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository

@HiltAndroidApp
class Puzzle15App : Application() {

    /** Musiqa ilova bilan birga boshlanishi uchun darhol yaratiladi. */
    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    /** Akkaunt yoki ism o'zgarishi bilan natijalarni qayta yuborishi uchun darhol yaratiladi. */
    @Inject
    lateinit var leaderboardRepository: LeaderboardRepository
}
