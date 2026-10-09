package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.BestResult
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats

/** Finished games stored on this device. */
interface RecordsRepository {
    suspend fun addResult(size: BoardSize, moves: Int, seconds: Long, stars: Int)

    suspend fun getBest(size: BoardSize): BestResult?

    fun observeStats(size: BoardSize): Flow<PersonalStats?>

    suspend fun clearAll()
}
