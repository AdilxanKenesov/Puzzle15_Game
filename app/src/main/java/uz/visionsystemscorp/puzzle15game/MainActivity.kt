package uz.visionsystemscorp.puzzle15game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.navigation.AppNavHost
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigationHandler
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.LocalClickSound

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: AppNavigationHandler

    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Puzzle15Theme {
                CompositionLocalProvider(LocalClickSound provides feedbackRepository::playClick) {
                    AppNavHost(handler = navigationHandler)
                }
            }
        }
    }
}
