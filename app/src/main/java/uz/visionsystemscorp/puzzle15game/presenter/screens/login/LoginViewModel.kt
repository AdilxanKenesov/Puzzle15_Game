package uz.visionsystemscorp.puzzle15game.presenter.screens.login

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.orbitContainer
import uz.visionsystemscorp.puzzle15game.domain.usecase.login.LoginUseCase
import uz.visionsystemscorp.puzzle15game.navigation.LoginRoute
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContract.LoginEvent
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContract.SideEffect

@HiltViewModel(assistedFactory = LoginViewModel.Factory::class)
class LoginViewModel @AssistedInject constructor(
    @Assisted route: LoginRoute,
    private val direction: LoginContract.Direction,
    private val loginUseCase: LoginUseCase,
) : ViewModel(), LoginContract.LoginViewModel {

    override val container = orbitContainer<LoginContract.LoginUiState, SideEffect>(
        LoginContract.LoginUiState(isOnboarding = route.isOnboarding),
    )

    override fun onEventDispatcher(event: LoginEvent) {
        when (event) {
            LoginEvent.GoogleClicked -> intent {
                if (state.isLoading) return@intent
                reduce { state.copy(isLoading = true) }
                postSideEffect(SideEffect.LaunchGoogleSignIn)
            }

            is LoginEvent.GoogleTokenReceived -> intent {
                loginUseCase.signInWithGoogle(event.idToken)
                    .onSuccess { hasName ->
                        reduce { state.copy(isLoading = false) }
                        proceed(hasName)
                    }
                    .onFailure {
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(SideEffect.ShowSignInError)
                    }
            }

            LoginEvent.GoogleCancelled -> intent { reduce { state.copy(isLoading = false) } }

            LoginEvent.GoogleFailed -> intent {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SideEffect.ShowSignInError)
            }

            LoginEvent.GuestClicked -> intent {
                if (state.isLoading) return@intent
                reduce { state.copy(isLoading = true) }
                loginUseCase.signInAsGuest()
                reduce { state.copy(isLoading = false) }
                proceed(hasName = false)
            }
        }
    }

    /** Birinchi kirishda akkauntda ism bo'lmasa Name ekraniga o'tiladi; Settings'dan kelinganda orqaga qaytiladi. */
    private fun proceed(hasName: Boolean) {
        when {
            !container.stateFlow.value.isOnboarding -> direction.back()
            hasName -> {
                loginUseCase.completeOnboarding()
                direction.openMain()
            }
            else -> direction.openName()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: LoginRoute): LoginViewModel
    }
}
