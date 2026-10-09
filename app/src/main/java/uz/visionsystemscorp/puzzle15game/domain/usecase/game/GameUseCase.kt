package uz.visionsystemscorp.puzzle15game.domain.usecase.game

import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame

interface GameUseCase {
    suspend fun start(size: BoardSize): SavedGame

    suspend fun restart(size: BoardSize): SavedGame

    suspend fun saveProgress(game: SavedGame)

    /** Natijani saqlaydi (yangi rekord bo'lsa reytingga yuboradi), saqlangan o'yinni tozalaydi va g'alaba effektini chaladi. */
    suspend fun finish(size: BoardSize, moves: Int, seconds: Long): GameResult

    fun onTileMoved()
}
