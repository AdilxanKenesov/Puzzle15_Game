package uz.visionsystemscorp.puzzle15game.data.repository_impl

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.visionsystemscorp.puzzle15game.data.mapper.toBest
import uz.visionsystemscorp.puzzle15game.data.mapper.toDomain
import uz.visionsystemscorp.puzzle15game.data.source.local.room.dao.GameResultDao
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.GameResultEntity
import uz.visionsystemscorp.puzzle15game.domain.model.BestResult
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository

@Singleton
class RecordsRepositoryImpl @Inject constructor(
    private val gameResultDao: GameResultDao,
) : RecordsRepository {

    override suspend fun addResult(size: BoardSize, moves: Int, seconds: Long, stars: Int) {
        gameResultDao.insert(
            GameResultEntity(
                boardDimension = size.dimension,
                moves = moves,
                seconds = seconds,
                stars = stars,
                finishedAt = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun getBest(size: BoardSize): BestResult? = gameResultDao.getBest(size.dimension)?.toBest()

    override fun observeStats(size: BoardSize): Flow<PersonalStats?> =
        gameResultDao.observeStats(size.dimension).map { it.toDomain() }

    override suspend fun clearAll() = gameResultDao.deleteAll()
}
