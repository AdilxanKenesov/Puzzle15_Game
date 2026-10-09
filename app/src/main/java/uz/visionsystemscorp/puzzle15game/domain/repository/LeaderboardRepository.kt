package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard

interface LeaderboardRepository {
    fun observe(size: BoardSize): Flow<Leaderboard>

    fun publishBest(size: BoardSize)
}
