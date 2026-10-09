package uz.visionsystemscorp.puzzle15game.domain.model

/**
 * @property parMoves finishing within this many moves earns three stars.
 */
enum class BoardSize(val dimension: Int, val parMoves: Int) {
    THREE(3, 40),
    FOUR(4, 100),
    FIVE(5, 250);

    val cellCount: Int get() = dimension * dimension

    /** The next smaller size; null on the smallest. */
    fun previous(): BoardSize? = entries.getOrNull(ordinal - 1)

    /** The next bigger size; null on the biggest. */
    fun next(): BoardSize? = entries.getOrNull(ordinal + 1)

    companion object {
        val Default = FOUR

        fun fromDimension(dimension: Int): BoardSize =
            entries.firstOrNull { it.dimension == dimension } ?: Default
    }
}
