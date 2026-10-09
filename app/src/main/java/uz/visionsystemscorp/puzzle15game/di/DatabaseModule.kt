package uz.visionsystemscorp.puzzle15game.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uz.visionsystemscorp.puzzle15game.data.source.local.room.AppDatabase
import uz.visionsystemscorp.puzzle15game.data.source.local.room.dao.GameResultDao
import uz.visionsystemscorp.puzzle15game.data.source.local.room.dao.SavedGameDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()

    @Provides
    fun provideGameResultDao(database: AppDatabase): GameResultDao = database.gameResultDao()

    @Provides
    fun provideSavedGameDao(database: AppDatabase): SavedGameDao = database.savedGameDao()
}
