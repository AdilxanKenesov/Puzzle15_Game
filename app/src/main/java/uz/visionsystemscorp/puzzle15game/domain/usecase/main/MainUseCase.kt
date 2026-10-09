package uz.visionsystemscorp.puzzle15game.domain.usecase.main

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.MainData

interface MainUseCase {
    fun observe(): Flow<MainData>

    fun selectPreviousSize()

    fun selectNextSize()
}
