package uz.visionsystemscorp.puzzle15game.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import uz.visionsystemscorp.puzzle15game.domain.model.Account
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion
import uz.visionsystemscorp.puzzle15game.domain.model.BestResult
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.Leaderboard
import uz.visionsystemscorp.puzzle15game.domain.model.PersonalStats
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import uz.visionsystemscorp.puzzle15game.domain.model.UserSettings
import uz.visionsystemscorp.puzzle15game.domain.repository.AuthRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.FeedbackRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.GameRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.LeaderboardRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.ProfileRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.RecordsRepository
import uz.visionsystemscorp.puzzle15game.domain.repository.SettingsRepository

class FakeGameRepository : GameRepository {
    val saved = mutableMapOf<BoardSize, SavedGame>()

    override suspend fun getSaved(size: BoardSize) = saved[size]

    override suspend fun save(game: SavedGame) {
        saved[game.board.size] = game
    }

    override suspend fun clear(size: BoardSize) {
        saved.remove(size)
    }

    override suspend fun clearAll() = saved.clear()
}

class FakeRecordsRepository : RecordsRepository {
    data class Entry(val size: BoardSize, val moves: Int, val seconds: Long, val stars: Int)

    val results = mutableListOf<Entry>()

    override suspend fun addResult(size: BoardSize, moves: Int, seconds: Long, stars: Int) {
        results += Entry(size, moves, seconds, stars)
    }

    override suspend fun getBest(size: BoardSize): BestResult? =
        results.filter { it.size == size }
            .minWithOrNull(compareBy({ it.moves }, { it.seconds }))
            ?.let { BestResult(it.moves, it.seconds) }

    override fun observeStats(size: BoardSize): Flow<PersonalStats?> = flowOf(null)

    override suspend fun clearAll() = results.clear()
}

class FakeSettingsRepository : SettingsRepository {
    private val state = MutableStateFlow(UserSettings())
    override val settings: StateFlow<UserSettings> = state

    override fun update(transform: (UserSettings) -> UserSettings) = state.update(transform)
}

class FakeProfileRepository : ProfileRepository {
    private val state = MutableStateFlow(PlayerProfile())
    var onboardingDone = false

    override val profile: StateFlow<PlayerProfile> = state

    /** What [syncName] finds in the signed-in account. */
    var remoteName: String? = null

    override fun setName(name: String) = state.update { it.copy(name = name) }

    override suspend fun syncName() {
        remoteName?.let(::setName)
    }

    override fun isOnboardingDone() = onboardingDone

    override fun setOnboardingDone() {
        onboardingDone = true
    }

    override fun reset() {
        onboardingDone = false
        setName("")
    }
}

class FakeFeedbackRepository : FeedbackRepository {
    var clicks = 0
    var moves = 0
    var wins = 0

    override fun playClick() {
        clicks++
    }

    override fun playMove() {
        moves++
    }

    override fun playWin() {
        wins++
    }
}

class FakeAuthRepository : AuthRepository {
    private val state = MutableStateFlow<Account?>(null)
    var failGoogle = false
    var restores = 0

    override val account: StateFlow<Account?> = state

    override suspend fun signInAsGuest(): Result<Account> =
        Result.success(state.value ?: Account("guest", isGuest = true).also { state.value = it })

    override suspend fun signInWithGoogle(idToken: String): Result<Account> =
        if (failGoogle) {
            Result.failure(IllegalStateException("offline"))
        } else {
            Result.success(Account("google", isGuest = false).also { state.value = it })
        }

    override suspend fun signOut() {
        state.value = null
    }

    override fun restoreSession() {
        restores++
    }

    /** What the next [deleteAccount] call without a token returns. */
    var deletion = AccountDeletion.DELETED
    val deletionTokens = mutableListOf<String?>()

    override suspend fun deleteAccount(googleIdToken: String?): AccountDeletion {
        deletionTokens += googleIdToken
        val result = if (googleIdToken != null) AccountDeletion.DELETED else deletion
        if (result == AccountDeletion.DELETED) state.value = null
        return result
    }
}

class FakeLeaderboardRepository : LeaderboardRepository {
    val published = mutableListOf<BoardSize>()

    override fun observe(size: BoardSize): Flow<Leaderboard> = flowOf(Leaderboard())

    override fun publishBest(size: BoardSize) {
        published += size
    }
}
