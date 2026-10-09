package uz.visionsystemscorp.puzzle15game.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.visionsystemscorp.puzzle15game.data.source.local.room.dao.GameResultDao
import uz.visionsystemscorp.puzzle15game.data.source.local.room.dao.SavedGameDao
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.GameResultEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.SavedGameEntity

@Database(
    entities = [GameResultEntity::class, SavedGameEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameResultDao(): GameResultDao
    abstract fun savedGameDao(): SavedGameDao

    companion object {
        const val NAME = "puzzle15.db"
    }
}
