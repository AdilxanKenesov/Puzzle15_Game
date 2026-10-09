package uz.visionsystemscorp.puzzle15game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.game.GameDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.info.InfoContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.info.InfoDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.main.MainContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.main.MainDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.name.NameContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.name.NameDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.records.RecordsDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.splash.SplashContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.splash.SplashDirection
import uz.visionsystemscorp.puzzle15game.presenter.screens.win.WinContract
import uz.visionsystemscorp.puzzle15game.presenter.screens.win.WinDirection

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionsModule {

    @Binds
    @ViewModelScoped
    fun bindSplashDirection(impl: SplashDirection): SplashContract.Direction

    @Binds
    @ViewModelScoped
    fun bindLoginDirection(impl: LoginDirection): LoginContract.Direction

    @Binds
    @ViewModelScoped
    fun bindNameDirection(impl: NameDirection): NameContract.Direction

    @Binds
    @ViewModelScoped
    fun bindMainDirection(impl: MainDirection): MainContract.Direction

    @Binds
    @ViewModelScoped
    fun bindGameDirection(impl: GameDirection): GameContract.Direction

    @Binds
    @ViewModelScoped
    fun bindWinDirection(impl: WinDirection): WinContract.Direction

    @Binds
    @ViewModelScoped
    fun bindRecordsDirection(impl: RecordsDirection): RecordsContract.Direction

    @Binds
    @ViewModelScoped
    fun bindSettingsDirection(impl: SettingsDirection): SettingsContract.Direction

    @Binds
    @ViewModelScoped
    fun bindInfoDirection(impl: InfoDirection): InfoContract.Direction
}
