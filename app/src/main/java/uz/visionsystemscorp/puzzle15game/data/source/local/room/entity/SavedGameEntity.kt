package uz.visionsystemscorp.puzzle15game.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_games")
data class SavedGameEntity(
    @PrimaryKey @ColumnInfo(name = "board_dimension") val boardDimension: Int,
    /** Row-major tiles joined with commas, 0 = blank. */
    @ColumnInfo(name = "tiles") val tiles: String,
    @ColumnInfo(name = "moves") val moves: Int,
    @ColumnInfo(name = "seconds") val seconds: Long,
)
