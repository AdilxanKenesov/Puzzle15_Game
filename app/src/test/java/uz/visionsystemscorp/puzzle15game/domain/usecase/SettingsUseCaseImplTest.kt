package uz.visionsystemscorp.puzzle15game.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.domain.model.AccountDeletion
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.SavedGame
import uz.visionsystemscorp.puzzle15game.domain.usecase.settings.SettingsUseCaseImpl

class SettingsUseCaseImplTest {

    private val profile = FakeProfileRepository()
    private val auth = FakeAuthRepository()
    private val records = FakeRecordsRepository()
    private val games = FakeGameRepository()
    private val useCase = SettingsUseCaseImpl(profile, FakeSettingsRepository(), auth, records, games)

    private suspend fun signedInPlayerWithProgress() {
        auth.signInWithGoogle("token")
        profile.setName("Aziza")
        profile.setOnboardingDone()
        records.addResult(BoardSize.FOUR, moves = 90, seconds = 60, stars = 3)
        games.save(SavedGame(Board.solved(BoardSize.THREE).slide(7)!!, moves = 1, seconds = 2))
    }

    @Test
    fun `guest has an account to delete but is not signed in with google`() = runTest {
        auth.signInAsGuest()
        val data = useCase.observe().first()
        assertTrue(data.hasAccount)
        assertFalse(data.isSignedIn)
    }

    @Test
    fun `deleting the account wipes local progress and restarts onboarding`() = runTest {
        signedInPlayerWithProgress()
        assertEquals(AccountDeletion.DELETED, useCase.deleteAccount())
        assertNull(auth.account.value)
        assertTrue(records.results.isEmpty())
        assertTrue(games.saved.isEmpty())
        assertEquals("", profile.profile.value.name)
        assertFalse(profile.onboardingDone)
    }

    @Test
    fun `local progress survives until a needed re-authentication succeeds`() = runTest {
        signedInPlayerWithProgress()
        auth.deletion = AccountDeletion.NEEDS_REAUTH
        assertEquals(AccountDeletion.NEEDS_REAUTH, useCase.deleteAccount())
        assertEquals(1, records.results.size)
        assertTrue(profile.onboardingDone)

        assertEquals(AccountDeletion.DELETED, useCase.deleteAccount(googleIdToken = "fresh"))
        assertEquals(listOf(null, "fresh"), auth.deletionTokens)
        assertTrue(records.results.isEmpty())
    }

    @Test
    fun `signing out resets this phone so the next player starts clean`() = runTest {
        signedInPlayerWithProgress()
        useCase.signOut()
        assertNull(auth.account.value)
        assertTrue(records.results.isEmpty())
        assertTrue(games.saved.isEmpty())
        assertFalse(profile.onboardingDone)
    }

    @Test
    fun `failed deletion keeps everything`() = runTest {
        signedInPlayerWithProgress()
        auth.deletion = AccountDeletion.FAILED
        assertEquals(AccountDeletion.FAILED, useCase.deleteAccount())
        assertEquals("Aziza", profile.profile.value.name)
        assertEquals(1, games.saved.size)
    }
}
