package uz.visionsystemscorp.puzzle15game.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.GameResultEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.StatsRow

@Dao
interface GameResultDao {

    @Insert
    suspend fun insert(result: GameResultEntity)

    @Query(
        """
        SELECT * FROM game_results
        WHERE board_dimension = :dimension
        ORDER BY moves ASC, seconds ASC
        LIMIT 1
        """,
    )
    suspend fun getBest(dimension: Int): GameResultEntity?

    @Query(
        """
        SELECT COUNT(*) AS gamesPlayed,
               MIN(moves) AS bestMoves,
               MIN(seconds) AS bestSeconds,
               AVG(moves) AS averageMoves,
               SUM(stars) AS totalStars
        FROM game_results
        WHERE board_dimension = :dimension
        """,
    )
    fun observeStats(dimension: Int): Flow<StatsRow>

    @Query("DELETE FROM game_results")
    suspend fun deleteAll()
}
