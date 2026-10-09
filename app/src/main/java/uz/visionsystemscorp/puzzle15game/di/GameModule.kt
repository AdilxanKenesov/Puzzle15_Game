package uz.visionsystemscorp.puzzle15game.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uz.visionsystemscorp.puzzle15game.domain.game.GameEngine

@Module
@InstallIn(SingletonComponent::class)
object GameModule {

    @Provides
    @Singleton
    fun provideGameEngine(): GameEngine = GameEngine()
}
