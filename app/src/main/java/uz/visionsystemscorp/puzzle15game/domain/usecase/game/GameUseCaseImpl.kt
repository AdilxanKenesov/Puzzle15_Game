package uz.visionsystemscorp.puzzle15game.domain.usecase.game

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.game.GameEngine
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.GameRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository

class GameUseCaseImpl @Inject constructor(
    private val engine: GameEngine,
    private val gameRepository: GameRepository,
    private val recordsRepository: RecordsRepository,
    private val feedbackRepository: FeedbackRepository,
    private val leaderboardRepository: LeaderboardRepository,
) : GameUseCase {

    override suspend fun start(size: BoardSize): SavedGame =
        gameRepository.getSaved(size) ?: fresh(size)

    override suspend fun restart(size: BoardSize): SavedGame {
        gameRepository.clear(size)
        return fresh(size)
    }

    override suspend fun saveProgress(game: SavedGame) {
        // Bitta ham yurilmagan taxtani saqlashning ma'nosi yo'q.
        if (game.moves > 0) gameRepository.save(game) else gameRepository.clear(game.board.size)
    }

    override suspend fun finish(size: BoardSize, moves: Int, seconds: Long): GameResult {
        val previousBest = recordsRepository.getBest(size)
        val stars = engine.stars(size, moves)
        recordsRepository.addResult(size, moves, seconds, stars)
        gameRepository.clear(size)
        feedbackRepository.playWin()
        val isNewBest = previousBest == null || previousBest.isBeatenBy(moves, seconds)
        if (isNewBest) leaderboardRepository.publishBest(size)
        return GameResult(size, moves, seconds, stars, isNewBest)
    }

    override fun onTileMoved() = feedbackRepository.playMove()

    private fun fresh(size: BoardSize) = SavedGame(engine.newBoard(size), moves = 0, seconds = 0)
}
