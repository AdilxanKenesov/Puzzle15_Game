package uz.visionsystemscorp.puzzle15game.domain.usecase.splash

import uz.visionsystemscorp.puzzle15game.domain.model.AppStart

interface SplashUseCase {
    /** Also brings back a lost guest session for returning players. */
    fun resolveStart(): AppStart
}
