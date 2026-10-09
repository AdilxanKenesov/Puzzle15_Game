package uz.visionsystemscorp.puzzle15game.data.repository_impl

import javax.inject.Inject
import javax.inject.Singleton
import uz.visionsystemscorp.puzzle15game.data.mapper.toDomain
import uz.visionsystemscorp.puzzle15game.data.mapper.toEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.dao.SavedGameDao
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import uz.visionsystemscorp.puzzle15game.domain.repository.GameRepository

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val savedGameDao: SavedGameDao,
) : GameRepository {

    override suspend fun getSaved(size: BoardSize): SavedGame? =
        savedGameDao.get(size.dimension)?.toDomain()

    override suspend fun save(game: SavedGame) = savedGameDao.upsert(game.toEntity())

    override suspend fun clear(size: BoardSize) = savedGameDao.delete(size.dimension)

    override suspend fun clearAll() = savedGameDao.deleteAll()
}
