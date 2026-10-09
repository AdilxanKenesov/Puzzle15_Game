package uz.visionsystemscorp.puzzle15game.domain.model

/**
 * @property parMoves shuncha yurishda yechilsa uchta yulduz beriladi.
 */
enum class BoardSize(val dimension: Int, val parMoves: Int) {
    THREE(3, 40),
    FOUR(4, 100),
    FIVE(5, 250);

    val cellCount: Int get() = dimension * dimension

    fun previous(): BoardSize? = entries.getOrNull(ordinal - 1)

    fun next(): BoardSize? = entries.getOrNull(ordinal + 1)

    companion object {
        val Default = FOUR

        fun fromDimension(dimension: Int): BoardSize =
            entries.firstOrNull { it.dimension == dimension } ?: Default
    }
}
