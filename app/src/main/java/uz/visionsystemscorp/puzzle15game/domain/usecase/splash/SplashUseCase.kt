package uz.visionsystemscorp.puzzle15game.domain.usecase.splash

import uz.visionsystemscorp.puzzle15game.domain.model.AppStart

interface SplashUseCase {
    /** Qaytgan o'yinchining yo'qolgan mehmon sessiyasini ham tiklaydi. */
    fun resolveStart(): AppStart
}
