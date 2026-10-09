package uz.visionsystemscorp.puzzle15game.presenter.screens.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.model.AppStart
import uz.visionsystemscorp.puzzle15game.domain.usecase.splash.SplashUseCase

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val direction: SplashContract.Direction,
    private val splashUseCase: SplashUseCase,
) : ViewModel(), SplashContract.SplashViewModel {

    override val container = orbitContainer<SplashContract.SplashUiState, SplashContract.SideEffect>(
        SplashContract.SplashUiState,
    )

    override fun onEventDispatcher(event: SplashContract.SplashEvent) {
        when (event) {
            SplashContract.SplashEvent.LoadingFinished -> when (splashUseCase.resolveStart()) {
                AppStart.ONBOARDING -> direction.openOnboarding()
                AppStart.MAIN -> direction.openMain()
            }
        }
    }
}
