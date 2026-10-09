package uz.visionsystemscorp.puzzle15game.domain.repository

import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame

/** Unfinished games, one slot per board size. */
interface GameRepository {
    suspend fun getSaved(size: BoardSize): SavedGame?

    suspend fun save(game: SavedGame)

    suspend fun clear(size: BoardSize)
}
