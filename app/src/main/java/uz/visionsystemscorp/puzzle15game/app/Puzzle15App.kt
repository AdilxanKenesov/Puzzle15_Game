package uz.visionsystemscorp.puzzle15game.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository

@HiltAndroidApp
class Puzzle15App : Application() {

    /** Injected eagerly so background music starts with the app. */
    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    /** Injected eagerly so it re-publishes results as soon as the account or name changes. */
    @Inject
    lateinit var leaderboardRepository: LeaderboardRepository
}
