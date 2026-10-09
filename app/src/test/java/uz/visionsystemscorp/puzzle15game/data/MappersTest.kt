package uz.visionsystemscorp.puzzle15game.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.data.mapper.toDomain
import uz.visionsystemscorp.puzzle15game.data.mapper.toEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.SavedGameEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.StatsRow
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame

class MappersTest {

    @Test
    fun `saved game survives a round trip`() {
        val game = SavedGame(Board.solved(BoardSize.FIVE).slide(23)!!, moves = 4, seconds = 12)
        assertEquals(game, game.toEntity().toDomain())
    }

    @Test
    fun `corrupted saved game is ignored`() {
        assertNull(SavedGameEntity(4, "1,2,3", 1, 1).toDomain())
        assertNull(SavedGameEntity(4, (0..15).joinToString(",").replace("15", "14"), 1, 1).toDomain())
        assertNull(SavedGameEntity(7, "0", 1, 1).toDomain())
    }

    @Test
    fun `stats map only when games exist`() {
        assertNull(StatsRow(0, null, null, null, null).toDomain())
        val stats = StatsRow(3, 80, 61, 92.6, 7).toDomain()!!
        assertEquals(93, stats.averageMoves)
        assertEquals(7, stats.totalStars)
        assertNull(stats.percentile)
    }
}
