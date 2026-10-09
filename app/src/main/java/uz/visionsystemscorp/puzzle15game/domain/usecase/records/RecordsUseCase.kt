package uz.visionsystemscorp.puzzle15game.domain.usecase.records

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.RecordsData

interface RecordsUseCase {
    fun observe(size: BoardSize): Flow<RecordsData>
}
