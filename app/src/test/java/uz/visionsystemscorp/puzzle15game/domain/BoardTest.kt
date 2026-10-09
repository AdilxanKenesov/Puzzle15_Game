package uz.visionsystemscorp.puzzle15game.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize

class BoardTest {

    @Test
    fun `solved board is solved`() {
        BoardSize.entries.forEach { assertTrue(Board.solved(it).isSolved) }
    }

    @Test
    fun `tile next to the blank slides into it`() {
        val board = Board.solved(BoardSize.THREE) // blank at index 8
        val moved = board.slide(7)
        assertNotNull(moved)
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 0, 8), moved!!.tiles)
        assertFalse(moved.isSolved)
    }

    @Test
    fun `tile above the blank slides down`() {
        val moved = Board.solved(BoardSize.THREE).slide(5)
        assertEquals(listOf(1, 2, 3, 4, 5, 0, 7, 8, 6), moved!!.tiles)
    }

    @Test
    fun `non adjacent tile does not move`() {
        val board = Board.solved(BoardSize.THREE)
        assertNull(board.slide(0))
        assertNull(board.slide(4)) // diagonal
        assertNull(board.slide(8)) // the blank itself
        assertNull(board.slide(42))
    }

    @Test
    fun `row wrap is not adjacency`() {
        // Blank at index 3 (row 1, col 0); index 2 is row 0, col 2 — neighbours in the list, not on the board.
        val board = Board(BoardSize.THREE, listOf(1, 2, 3, 0, 4, 5, 6, 7, 8))
        assertNull(board.slide(2))
        assertNotNull(board.slide(4))
    }

    @Test
    fun `sliding back restores the board`() {
        val board = Board.solved(BoardSize.FOUR)
        assertEquals(board, board.slide(14)!!.slide(15))
    }

    @Test
    fun `in place reflects the target cell`() {
        val board = Board(BoardSize.THREE, listOf(1, 2, 3, 4, 5, 6, 7, 0, 8))
        assertTrue(board.isInPlace(0))
        assertFalse(board.isInPlace(7))
        assertFalse(board.isInPlace(8))
    }

    @Test
    fun `size steps stop at the smallest and biggest board`() {
        assertEquals(null, BoardSize.THREE.previous())
        assertEquals(BoardSize.FOUR, BoardSize.THREE.next())
        assertEquals(BoardSize.FOUR, BoardSize.FIVE.previous())
        assertEquals(null, BoardSize.FIVE.next())
    }
}
