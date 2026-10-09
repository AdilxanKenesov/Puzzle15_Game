package uz.visionsystemscorp.puzzle15game.domain.model

import kotlin.math.abs

/**
 * Toshlar qatorma-qator joylashgan; [EMPTY] bo'sh katak.
 * Toshlar 1, 2, … n²-1 tartibida va oxirida bo'sh katak bo'lsa, taxta yechilgan.
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

    /** [index] dagi toshni bo'sh katakka suradi; tosh bo'sh katak yonida bo'lmasa null qaytaradi. */
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
