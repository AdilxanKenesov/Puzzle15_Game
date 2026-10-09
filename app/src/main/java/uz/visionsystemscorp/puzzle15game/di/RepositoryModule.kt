package uz.visionsystemscorp.puzzle15game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uz.visionsystemscorp.puzzle15game.data.repository_impl.AuthRepositoryImpl
import uz.visionsystemscorp.puzzle15game.data.repository_impl.FeedbackRepositoryImpl
import uz.visionsystemscorp.puzzle15game.data.repository_impl.GameRepositoryImpl
import uz.visionsystemscorp.puzzle15game.data.repository_impl.LeaderboardRepositoryImpl
import uz.visionsystemscorp.puzzle15game.data.repository_impl.ProfileRepositoryImpl
import uz.visionsystemscorp.puzzle15game.data.repository_impl.RecordsRepositoryImpl
import uz.visionsystemscorp.puzzle15game.data.repository_impl.SettingsRepositoryImpl
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.GameRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.SettingsRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun bindGameRepository(impl: GameRepositoryImpl): GameRepository

    @Binds
    @Singleton
    fun bindRecordsRepository(impl: RecordsRepositoryImpl): RecordsRepository

    @Binds
    @Singleton
    fun bindLeaderboardRepository(impl: LeaderboardRepositoryImpl): LeaderboardRepository

    @Binds
    @Singleton
    fun bindFeedbackRepository(impl: FeedbackRepositoryImpl): FeedbackRepository
}
