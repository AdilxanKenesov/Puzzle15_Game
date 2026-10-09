package uz.visionsystemscorp.puzzle15game.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "game_results",
    indices = [Index("board_dimension")],
)
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "board_dimension") val boardDimension: Int,
    @ColumnInfo(name = "moves") val moves: Int,
    @ColumnInfo(name = "seconds") val seconds: Long,
    @ColumnInfo(name = "stars") val stars: Int,
    @ColumnInfo(name = "finished_at") val finishedAt: Long,
)
