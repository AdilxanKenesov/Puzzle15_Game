package uz.visionsystemscorp.puzzle15game.presenter.screens.records

import javax.inject.Inject
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.navigation.AppNavigator
import uz.visionsystemscorp.puzzle15game.navigation.MainRoute
import uz.visionsystemscorp.puzzle15game.navigation.toGameRoute

class RecordsDirection @Inject constructor(
    private val navigator: AppNavigator,
) : RecordsContract.Direction {

    override fun backToMain() = navigator.backTo(MainRoute)

    /** Main'dan boshlab ochiladi, ostida eski o'yin ekrani qolib ketmasin. */
    override fun openGame(size: BoardSize) {
        navigator.backTo(MainRoute)
        navigator.navigateTo(size.toGameRoute())
    }
}
