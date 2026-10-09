package uz.visionsystemscorp.puzzle15game.domain.usecase

import kotlin.random.Random
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.domain.game.GameEngine
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import uz.visionsystemscorp.puzzle15game.domain.usecase.game.GameUseCaseImpl

class GameUseCaseImplTest {

    private val games = FakeGameRepository()
    private val records = FakeRecordsRepository()
    private val settings = FakeSettingsRepository()
    private val feedback = FakeFeedbackRepository()
    private val leaderboard = FakeLeaderboardRepository()
    private val useCase = GameUseCaseImpl(GameEngine(Random(1)), games, records, settings, feedback, leaderboard)

    @Test
    fun `start resumes the saved game`() = runTest {
        val saved = SavedGame(Board.solved(BoardSize.FOUR).slide(14)!!, moves = 7, seconds = 30)
        games.save(saved)
        assertEquals(saved, useCase.start(BoardSize.FOUR))
    }

    @Test
    fun `start without a save shuffles a fresh board`() = runTest {
        val game = useCase.start(BoardSize.THREE)
        assertEquals(0, game.moves)
        assertEquals(0, game.seconds)
        assertFalse(game.board.isSolved)
    }

    @Test
    fun `restart drops the saved game`() = runTest {
        val saved = SavedGame(Board.solved(BoardSize.FOUR).slide(14)!!, moves = 7, seconds = 30)
        games.save(saved)
        val fresh = useCase.restart(BoardSize.FOUR)
        assertNull(games.saved[BoardSize.FOUR])
        assertNotEquals(saved, fresh)
    }

    @Test
    fun `untouched board is not saved`() = runTest {
        val game = useCase.start(BoardSize.FOUR)
        useCase.saveProgress(game)
        assertNull(games.saved[BoardSize.FOUR])
        useCase.saveProgress(game.copy(moves = 1))
        assertEquals(1, games.saved[BoardSize.FOUR]?.moves)
    }

    @Test
    fun `finish records the result, clears progress and celebrates`() = runTest {
        games.save(SavedGame(Board.solved(BoardSize.FOUR).slide(14)!!, 3, 5))
        val result = useCase.finish(BoardSize.FOUR, moves = 90, seconds = 60)
        assertEquals(3, result.stars)
        assertTrue(result.isNewBest)
        assertEquals(1, records.results.size)
        assertNull(games.saved[BoardSize.FOUR])
        assertEquals(1, feedback.wins)
    }

    @Test
    fun `new best needs fewer moves or the same moves faster`() = runTest {
        useCase.finish(BoardSize.FOUR, moves = 90, seconds = 60)
        assertFalse(useCase.finish(BoardSize.FOUR, moves = 95, seconds = 10).isNewBest)
        assertFalse(useCase.finish(BoardSize.FOUR, moves = 90, seconds = 60).isNewBest)
        assertTrue(useCase.finish(BoardSize.FOUR, moves = 90, seconds = 59).isNewBest)
        assertTrue(useCase.finish(BoardSize.FOUR, moves = 80, seconds = 99).isNewBest)
        // Bests are per board size.
        assertTrue(useCase.finish(BoardSize.FIVE, moves = 400, seconds = 400).isNewBest)
    }

    @Test
    fun `only a new best is published to the world leaderboard`() = runTest {
        useCase.finish(BoardSize.FOUR, moves = 90, seconds = 60)
        useCase.finish(BoardSize.FOUR, moves = 95, seconds = 10)
        useCase.finish(BoardSize.FOUR, moves = 80, seconds = 99)
        assertEquals(listOf(BoardSize.FOUR, BoardSize.FOUR), leaderboard.published)
    }

    @Test
    fun `audio toggle mutes and unmutes music and sound together`() = runTest {
        assertTrue(useCase.observeAudio().first())
        useCase.toggleAudio()
        assertFalse(settings.settings.value.music)
        assertFalse(settings.settings.value.sound)
        useCase.toggleAudio()
        assertTrue(useCase.observeAudio().first())
    }

    @Test
    fun `tile move triggers move feedback`() {
        useCase.onTileMoved()
        assertEquals(1, feedback.moves)
    }
}
