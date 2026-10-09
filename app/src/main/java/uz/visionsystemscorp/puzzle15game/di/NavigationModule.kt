package uz.visionsystemscorp.puzzle15game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigationDispatcher
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigationHandler
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigator(impl: AppNavigationDispatcher): AppNavigator

    @Binds
    fun bindAppNavigationHandler(impl: AppNavigationDispatcher): AppNavigationHandler
}
