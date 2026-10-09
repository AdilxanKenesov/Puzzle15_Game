package uz.visionsystemscorp.puzzle15game.domain.usecase.game

import kotlinx.coroutines.flow.Flow
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame

interface GameUseCase {
    /** The unfinished game on [size] if there is one, otherwise a freshly shuffled board. */
    suspend fun start(size: BoardSize): SavedGame

    /** Drops any saved progress and shuffles a new board. */
    suspend fun restart(size: BoardSize): SavedGame

    suspend fun saveProgress(game: SavedGame)

    /** Records the finished game (publishing it if it is a new best), clears its saved progress and plays the win feedback. */
    suspend fun finish(size: BoardSize, moves: Int, seconds: Long): GameResult

    fun observeAudio(): Flow<Boolean>

    fun toggleAudio()

    fun onTileMoved()
}
