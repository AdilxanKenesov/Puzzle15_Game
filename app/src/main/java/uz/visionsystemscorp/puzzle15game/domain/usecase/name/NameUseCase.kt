package uz.visionsystemscorp.puzzle15game.domain.usecase.name

interface NameUseCase {
    fun currentName(): String

    fun saveName(name: String, completeOnboarding: Boolean)
}
