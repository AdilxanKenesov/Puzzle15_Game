package uz.visionsystemscorp.puzzle15game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.visionsystemscorp.puzzle15game.domain.usecase.game.GameUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.game.GameUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.login.LoginUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.login.LoginUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.main.MainUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.main.MainUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.name.NameUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.name.NameUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.records.RecordsUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.records.RecordsUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.settings.SettingsUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.settings.SettingsUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.splash.SplashUseCase
import uz.visionsystemscorp.puzzle15game.domain.usecase.splash.SplashUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindSplashUseCase(impl: SplashUseCaseImpl): SplashUseCase

    @Binds
    fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase

    @Binds
    fun bindNameUseCase(impl: NameUseCaseImpl): NameUseCase

    @Binds
    fun bindMainUseCase(impl: MainUseCaseImpl): MainUseCase

    @Binds
    fun bindGameUseCase(impl: GameUseCaseImpl): GameUseCase

    @Binds
    fun bindRecordsUseCase(impl: RecordsUseCaseImpl): RecordsUseCase

    @Binds
    fun bindSettingsUseCase(impl: SettingsUseCaseImpl): SettingsUseCase
}
