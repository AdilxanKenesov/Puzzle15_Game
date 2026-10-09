package uz.visionsystemscorp.puzzle15game.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard

/** World ranking per board size. */
interface LeaderboardRepository {
    fun observe(size: BoardSize): Flow<Leaderboard>

    /** Uploads this device's best result on [size] for the current account; no-op while signed out. */
    fun publishBest(size: BoardSize)
}
