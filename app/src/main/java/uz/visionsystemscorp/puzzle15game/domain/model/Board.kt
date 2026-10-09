package uz.visionsystemscorp.puzzle15game.domain.model

import kotlin.math.abs

/**
 * Row-major tile layout; [EMPTY] marks the blank cell.
 * Solved when tiles read 1, 2, … n²-1 followed by the blank.
 */
data class Board(val size: BoardSize, val tiles: List<Int>) {

    init {
        require(tiles.size == size.cellCount) { "Expected ${size.cellCount} tiles, got ${tiles.size}" }
    }

    val emptyIndex: Int get() = tiles.indexOf(EMPTY)

    val isSolved: Boolean
        get() = tiles.last() == EMPTY && (0 until tiles.lastIndex).all { tiles[it] == it + 1 }

    fun isInPlace(index: Int): Boolean = tiles[index] == index + 1

    fun canSlide(index: Int): Boolean {
        val dimension = size.dimension
        val empty = emptyIndex
        return abs(index / dimension - empty / dimension) + abs(index % dimension - empty % dimension) == 1
    }

    /** Moves the tile at [index] into the blank cell, or returns null if it is not adjacent. */
    fun slide(index: Int): Board? {
        if (index !in tiles.indices || !canSlide(index)) return null
        val next = tiles.toMutableList()
        next[emptyIndex] = tiles[index]
        next[index] = EMPTY
        return copy(tiles = next)
    }

    companion object {
        const val EMPTY = 0

        fun solved(size: BoardSize): Board =
            Board(size, (1 until size.cellCount) + EMPTY)
    }
}
