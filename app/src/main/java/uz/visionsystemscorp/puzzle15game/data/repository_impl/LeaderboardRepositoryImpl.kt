package uz.visionsystemscorp.puzzle15game.data.repository_impl

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import uz.visionsystemscorp.puzzle15game.data.mapper.toDomain
import uz.visionsystemscorp.puzzle15game.data.source.local.pref.SharedManager
import uz.visionsystemscorp.puzzle15game.data.source.remote.firestore.FirestoreSource
import uz.visionsystemscorp.puzzle15game.data.source.remote.firestore.LeaderboardEntryDto
import uz.visionsystemscorp.puzzle15game.di.ApplicationScope
import uz.visionsystemscorp.puzzle15game.domain.model.BestResult
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardEntry
import uz.visionsystemscorp.puzzle15game.domain.model.LeaderboardStatus
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository

@Singleton
class LeaderboardRepositoryImpl @Inject constructor(
    private val firestore: FirestoreSource,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val recordsRepository: RecordsRepository,
    private val sharedManager: SharedManager,
    @ApplicationScope private val scope: CoroutineScope,
) : LeaderboardRepository {

    init {
        // On launch and whenever the account or name changes, upload any best that the server has not
        // confirmed yet (the ledger in prefs skips the rest), so a failed or interrupted write heals itself.
        // A new name also renames the cloud entries in place, reaching results this device does not hold.
        scope.launch {
            var previous: Pair<String?, String>? = null
            combine(authRepository.account.map { it?.uid }, profileRepository.profile.map { it.name }, ::Pair)
                .distinctUntilChanged()
                .collect { current ->
                    val before = previous
                    previous = current
                    val (uid, name) = current
                    if (uid == null || !PlayerProfile.isValidName(name)) return@collect
                    BoardSize.entries.forEach { publish(it) }
                    if (before != null && before.first == uid && before.second != name) {
                        firestore.renameEntries(uid, name, BoardSize.entries.map { it.dimension })
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(size: BoardSize): Flow<Leaderboard> =
        combine(recordsRepository.observeStats(size), profileRepository.profile, authRepository.account) { stats, profile, account ->
            Triple(stats?.let { BestResult(it.bestMoves, it.bestSeconds) }, profile.name, account?.uid)
        }
            .distinctUntilChanged()
            .transformLatest { (best, name, uid) ->
                val localYou = best?.let { LeaderboardEntry(rank = null, name, it.moves, it.seconds) }
                emit(Leaderboard(you = localYou, status = LeaderboardStatus.LOADING))
                emit(
                    runCatching { fetch(size, uid, localYou) }
                        .getOrElse { Leaderboard(you = localYou, status = LeaderboardStatus.OFFLINE) },
                )
            }

    override fun publishBest(size: BoardSize) {
        scope.launch { publish(size) }
    }

    private suspend fun publish(size: BoardSize) {
        val uid = authRepository.account.value?.uid ?: return
        val name = profileRepository.profile.value.name
        if (!PlayerProfile.isValidName(name)) return
        val best = recordsRepository.getBest(size) ?: return
        val signature = "$uid|$name|${best.moves}|${best.seconds}"
        if (sharedManager.publishedBest(size.dimension) == signature) return
        firestore.putBest(uid, size.dimension, LeaderboardEntryDto(name, best.moves, best.seconds))
            .addOnSuccessListener { sharedManager.setPublishedBest(size.dimension, signature) }
    }

    private suspend fun fetch(size: BoardSize, uid: String?, localYou: LeaderboardEntry?): Leaderboard {
        val dimension = size.dimension
        val top = firestore.fetchTop(dimension, TOP_LIMIT).mapIndexed { i, entry -> entry.toDomain(rank = i + 1) }
        val mine = uid?.let { firestore.fetchEntry(dimension, it) }
            ?: return Leaderboard(top, localYou, status = LeaderboardStatus.READY)

        val rank = firestore.countAhead(dimension, mine.moves, mine.seconds) + 1
        val total = firestore.countAll(dimension)
        val percentile = if (total > 1) ((total - rank) * 100 / (total - 1)).toInt() else null
        return Leaderboard(top, mine.toDomain(rank.toInt()), percentile, LeaderboardStatus.READY)
    }

    private companion object {
        const val TOP_LIMIT = 50L
    }
}
