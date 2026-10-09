package uz.visionsystemscorp.puzzle15game.domain

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.domain.game.GameEngine
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize

class GameEngineTest {

    private val engine = GameEngine(Random(42))

    @Test
    fun `new boards are solvable, unsolved permutations`() {
        BoardSize.entries.forEach { size ->
            repeat(200) {
                val board = engine.newBoard(size)
                assertEquals((0 until size.cellCount).toList(), board.tiles.sorted())
                assertFalse(board.isSolved)
                assertTrue(engine.isSolvable(board.tiles, size.dimension))
            }
        }
    }

    @Test
    fun `solved layout is solvable`() {
        BoardSize.entries.forEach { size ->
            assertTrue(engine.isSolvable(Board.solved(size).tiles, size.dimension))
        }
    }

    @Test
    fun `swapping two numbers makes it unsolvable`() {
        BoardSize.entries.forEach { size ->
            val tiles = Board.solved(size).tiles.toMutableList()
            tiles[0] = 2
            tiles[1] = 1
            assertFalse(engine.isSolvable(tiles, size.dimension))
        }
    }

    @Test
    fun `classic 14-15 swap is unsolvable on 4x4`() {
        val tiles = (1..13).toList() + listOf(15, 14, 0)
        assertFalse(engine.isSolvable(tiles, 4))
    }

    @Test
    fun `any sequence of legal slides stays solvable`() {
        val random = Random(7)
        BoardSize.entries.forEach { size ->
            var board = Board.solved(size)
            repeat(500) {
                val candidates = board.tiles.indices.filter(board::canSlide)
                board = board.slide(candidates.random(random))!!
                assertTrue(engine.isSolvable(board.tiles, size.dimension))
            }
        }
    }

    @Test
    fun `stars follow par moves`() {
        val size = BoardSize.FOUR
        assertEquals(3, engine.stars(size, size.parMoves))
        assertEquals(2, engine.stars(size, size.parMoves + 1))
        assertEquals(2, engine.stars(size, size.parMoves * 2))
        assertEquals(1, engine.stars(size, size.parMoves * 2 + 1))
    }
}
