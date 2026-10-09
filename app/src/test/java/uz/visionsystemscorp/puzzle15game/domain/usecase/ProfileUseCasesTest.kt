package uz.visionsystemscorp.puzzle15game.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.visionsystemscorp.puzzle15game.domain.model.AppStart
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.domain.usecase.name.NameUseCaseImpl
import uz.visionsystemscorp.puzzle15game.domain.usecase.splash.SplashUseCaseImpl

class ProfileUseCasesTest {

    private val profile = FakeProfileRepository()
    private val auth = FakeAuthRepository()

    @Test
    fun `first launch starts with onboarding, later launches go to main`() {
        val splash = SplashUseCaseImpl(profile, auth)
        assertEquals(AppStart.ONBOARDING, splash.resolveStart())
        assertEquals(0, auth.restores)
        NameUseCaseImpl(profile).saveName("Adilxan", completeOnboarding = true)
        assertEquals(AppStart.MAIN, splash.resolveStart())
        assertEquals(1, auth.restores)
    }

    @Test
    fun `renaming from settings does not touch onboarding`() {
        NameUseCaseImpl(profile).saveName("  Aziza  ", completeOnboarding = false)
        assertEquals("Aziza", profile.profile.value.name)
        assertFalse(profile.onboardingDone)
    }

    @Test
    fun `name length rules`() {
        assertFalse(PlayerProfile.isValidName("ab"))
        assertFalse(PlayerProfile.isValidName("   ab   "))
        assertTrue(PlayerProfile.isValidName("abc"))
        assertTrue(PlayerProfile.isValidName("a".repeat(16)))
        assertFalse(PlayerProfile.isValidName("a".repeat(17)))
    }
}
