package uz.visionsystemscorp.puzzle15game.domain.usecase.name

interface NameUseCase {
    fun currentName(): String

    /** Stores [name]; on first launch also marks onboarding as finished. */
    fun saveName(name: String, completeOnboarding: Boolean)
}
