package uz.visionsystemscorp.puzzle15game.presenter

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import kotlin.random.Random
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.RobolectricTestRunner
import uz.visionsystemscorp.puzzle15game.domain.game.GameEngine
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardEntry
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardStatus
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.info.InfoContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.main.MainContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.main.MainContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.name.NameContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.name.NameContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.splash.SplashContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.win.WinContent
import uz.visionsystemscorp.puzzle15game.presenter.screens.win.WinContract
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme

/**
 * Renders every screen with sample data on the JVM. `./gradlew recordRoborazziDebug` saves them to
 * docs/screenshots (used by the README and the Play listing); a plain test run only checks they render.
 *
 * 360×720 dp at xxhdpi = 1080×2160 px, a 2:1 phone shot that Google Play accepts.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// A plain Application: the real one would start Firebase and sound through Hilt.
@Config(sdk = [35], qualifiers = "w360dp-h720dp-xxhdpi", application = Application::class)
class ScreenshotTest {

    @get:Rule
    val compose = createComposeRule()

    private fun capture(name: String, settleMillis: Long = 2_500, content: @Composable () -> Unit) {
        // Infinite animations (fireflies, swaying signs) never go idle, so drive the clock by hand.
        compose.mainClock.autoAdvance = false
        compose.setContent { Puzzle15Theme { content() } }
        compose.mainClock.advanceTimeBy(settleMillis)
        compose.onRoot().captureRoboImage("${ScreenshotDir}/$name.png")
    }

    @Test
    fun splash() = capture("01_splash", settleMillis = 1_500) { SplashContent(onEventDispatcher = {}) }

    @Test
    fun login() = capture("02_login") { LoginContent(LoginContract.LoginUiState(), onEventDispatcher = {}) }

    @Test
    fun name() = capture("03_name") {
        NameContent(NameContract.NameUiState(name = "Adilxan"), onEventDispatcher = {})
    }

    @Test
    fun main() = capture("04_main") {
        MainContent(MainContract.MainUiState(playerName = "Adilxan", boardSize = BoardSize.FOUR), onEventDispatcher = {})
    }

    @Test
    fun game() = capture("05_game") { GameContent(sampleGame(), onEventDispatcher = {}) }

    @Test
    fun pause() = capture("06_pause") { GameContent(sampleGame().copy(isPaused = true), onEventDispatcher = {}) }

    @Test
    fun win() = capture("07_win", settleMillis = 3_500) {
        WinContent(
            WinContract.WinUiState(GameResult(BoardSize.FOUR, moves = 86, seconds = 97, stars = 3, isNewBest = true)),
            onEventDispatcher = {},
        )
    }

    @Test
    fun recordsWorld() = capture("08_records_world") {
        RecordsContent(RecordsContract.RecordsUiState(leaderboard = sampleLeaderboard), onEventDispatcher = {})
    }

    @Test
    fun recordsMine() = capture("09_records_mine") {
        RecordsContent(
            RecordsContract.RecordsUiState(
                tab = RecordsContract.RecordsTab.Mine,
                leaderboard = sampleLeaderboard,
                personal = PersonalStats(bestMoves = 87, bestSeconds = 102, gamesPlayed = 24, averageMoves = 131, totalStars = 51, percentile = 82),
            ),
            onEventDispatcher = {},
        )
    }

    @Test
    fun settings() = capture("10_settings") {
        SettingsContent(
            SettingsContract.SettingsUiState(playerName = "Adilxan", isSignedIn = true, hasAccount = true),
            onEventDispatcher = {},
        )
    }

    @Test
    fun info() = capture("11_info") { InfoContent(onEventDispatcher = {}) }

    private fun sampleGame(): GameContract.GameUiState {
        // Fixed seed: the same shuffled board on every run.
        val board = GameEngine(Random(15)).newBoard(BoardSize.FOUR)
        return GameContract.GameUiState(size = BoardSize.FOUR, board = board, moves = 37, elapsedSeconds = 74)
    }

    private companion object {
        const val ScreenshotDir = "../docs/screenshots"

        val sampleLeaderboard = Leaderboard(
            entries = listOf(
                LeaderboardEntry(1, "Aziza", 48, 41),
                LeaderboardEntry(2, "Bekzod", 52, 47),
                LeaderboardEntry(3, "Malika", 57, 55),
                LeaderboardEntry(4, "Sardor", 61, 58),
                LeaderboardEntry(5, "Dilnoza", 64, 70),
            ),
            you = LeaderboardEntry(24, "Adilxan", 87, 102),
            percentile = 82,
            status = LeaderboardStatus.READY,
        )
    }
}
