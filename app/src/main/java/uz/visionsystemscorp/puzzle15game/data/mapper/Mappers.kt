package uz.visionsystemscorp.puzzle15game.data.mapper

import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.GameResultEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.SavedGameEntity
import uz.visionsystemscorp.puzzle15game.data.source.local.room.entity.StatsRow
import uz.visionsystemscorp.puzzle15game.data.source.remote.firestore.LeaderboardEntryDto
import uz.visionsystemscorp.puzzle15game.domain.model.BestResult
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardEntry
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import kotlin.math.roundToInt

fun SavedGame.toEntity() = SavedGameEntity(
    boardDimension = board.size.dimension,
    tiles = board.tiles.joinToString(","),
    moves = moves,
    seconds = seconds,
)

/** Null when the stored row does not describe a valid board (e.g. corrupted data). */
fun SavedGameEntity.toDomain(): SavedGame? {
    val size = BoardSize.entries.firstOrNull { it.dimension == boardDimension } ?: return null
    val tiles = tiles.split(",").mapNotNull { it.toIntOrNull() }
    if (tiles.sorted() != (0 until size.cellCount).toList()) return null
    return SavedGame(Board(size, tiles), moves, seconds)
}

fun GameResultEntity.toBest() = BestResult(moves, seconds)

fun StatsRow.toDomain(): PersonalStats? {
    if (gamesPlayed == 0) return null
    return PersonalStats(
        bestMoves = bestMoves ?: 0,
        bestSeconds = bestSeconds ?: 0,
        gamesPlayed = gamesPlayed,
        averageMoves = averageMoves?.roundToInt() ?: 0,
        totalStars = totalStars ?: 0,
    )
}

fun LeaderboardEntryDto.toDomain(rank: Int) = LeaderboardEntry(rank, name, moves, seconds)
