package uz.visionsystemscorp.puzzle15game.domain.game

import kotlin.random.Random
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize

class GameEngine(private val random: Random = Random.Default) {

    /** Random, solvable and not already solved layout. */
    fun newBoard(size: BoardSize): Board {
        val tiles = Board.solved(size).tiles.toMutableList()
        do {
            tiles.shuffle(random)
        } while (!isSolvable(tiles, size.dimension) || Board(size, tiles).isSolved)
        return Board(size, tiles.toList())
    }

    fun stars(size: BoardSize, moves: Int): Int = when {
        moves <= size.parMoves -> 3
        moves <= size.parMoves * 2 -> 2
        else -> 1
    }

    /**
     * Odd width: solvable iff the inversion count is even.
     * Even width: counting the blank's row from the bottom (1-based), solvable iff
     * that row is odd and inversions are even, or the row is even and inversions are odd.
     */
    internal fun isSolvable(tiles: List<Int>, dimension: Int): Boolean {
        val numbers = tiles.filter { it != Board.EMPTY }
        var inversions = 0
        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                if (numbers[i] > numbers[j]) inversions++
            }
        }
        if (dimension % 2 == 1) return inversions % 2 == 0
        val blankRowFromBottom = dimension - tiles.indexOf(Board.EMPTY) / dimension
        return if (blankRowFromBottom % 2 == 1) inversions % 2 == 0 else inversions % 2 == 1
    }
}
