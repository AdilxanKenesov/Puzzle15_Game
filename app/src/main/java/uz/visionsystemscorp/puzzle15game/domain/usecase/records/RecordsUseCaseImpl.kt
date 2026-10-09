package uz.visionsystemscorp.puzzle15game.domain.usecase.records

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.RecordsData
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository

class RecordsUseCaseImpl @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
    private val recordsRepository: RecordsRepository,
) : RecordsUseCase {

    override fun observe(size: BoardSize): Flow<RecordsData> =
        combine(leaderboardRepository.observe(size), recordsRepository.observeStats(size)) { leaderboard, stats ->
            RecordsData(leaderboard, stats?.copy(percentile = leaderboard.percentile))
        }
}
