package uz.visionsystemscorp.puzzle15game.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.domain.usecase.login.LoginUseCaseImpl

class LoginUseCaseImplTest {

    private val auth = FakeAuthRepository()
    private val profile = FakeProfileRepository()
    private val useCase = LoginUseCaseImpl(auth, profile)

    @Test
    fun `google account with a saved name skips the name screen`() = runTest {
        profile.remoteName = "Aziza"
        assertTrue(useCase.signInWithGoogle("token").getOrThrow())
        assertEquals("Aziza", profile.profile.value.name)
    }

    @Test
    fun `new google account still needs a name`() = runTest {
        assertFalse(useCase.signInWithGoogle("token").getOrThrow())
    }

    @Test
    fun `failed google sign-in is reported and leaves the player signed out`() = runTest {
        auth.failGoogle = true
        assertTrue(useCase.signInWithGoogle("token").isFailure)
        assertEquals(null, auth.account.value)
    }

    @Test
    fun `guest sign-in creates a guest account`() = runTest {
        useCase.signInAsGuest()
        assertTrue(auth.account.value!!.isGuest)
    }
}
