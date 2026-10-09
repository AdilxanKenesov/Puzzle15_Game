package uz.visionsystemscorp.puzzle15game.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.SavedGameEntity

@Dao
interface SavedGameDao {

    @Query("SELECT * FROM saved_games WHERE board_dimension = :dimension")
    suspend fun get(dimension: Int): SavedGameEntity?

    @Upsert
    suspend fun upsert(game: SavedGameEntity)

    @Query("DELETE FROM saved_games WHERE board_dimension = :dimension")
    suspend fun delete(dimension: Int)
}
