package uz.visionsystemscorp.puzzle15game.presenter.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.navigation.GameRoute
import uz.visionsystemscorp.puzzle15game.domain.game.GameEngine
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.pulse
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.swing
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ArtboardOverlay
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.BoardSizeBadge
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ImageIconButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.StatBox
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.formatElapsed

@Composable
fun GameScreen(route: GameRoute) {
    val viewModel: GameContract.GameViewModel =
        hiltViewModel<GameViewModel, GameViewModel.Factory>(creationCallback = { it.create(route) })
    val uiState = viewModel.collectAsState()
    val onEvent by rememberUpdatedState(viewModel::onEventDispatcher)

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { onEvent(GameContract.GameEvent.ScreenHidden) }
    DisposableEffect(Unit) {
        onDispose { onEvent(GameContract.GameEvent.ScreenHidden) }
    }
    GameContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
private fun GameContent(
    uiState: GameContract.GameUiState,
    onEventDispatcher: (GameContract.GameEvent) -> Unit,
) {
    val onEvent = onEventDispatcher
    ScreenBackground(
        scrimAlpha = 0.22f,
        showFireflies = true,
        overlay = {
            if (uiState.isPaused) {
                PauseOverlay(
                    onResume = { onEvent(GameContract.GameEvent.ResumeClicked) },
                    onHome = { onEvent(GameContract.GameEvent.HomeClicked) },
                    onSettings = { onEvent(GameContract.GameEvent.SettingsClicked) },
                )
            }
        },
    ) {
        Row(
            modifier = Modifier.place(14.dp, 18.dp, 362.dp, 68.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageIconButton(R.drawable.btn_home, stringResource(R.string.cd_home), { onEvent(GameContract.GameEvent.HomeClicked) }, Modifier.size(60.dp))
            BoardSizeBadge(uiState.size, Modifier.size(150.dp, 68.dp), fontSize = 26.sp, labelSize = 10.sp)
            ImageIconButton(R.drawable.btn_pause, stringResource(R.string.cd_pause), { onEvent(GameContract.GameEvent.PauseClicked) }, Modifier.size(60.dp, 58.dp))
        }

        Row(
            modifier = Modifier.place(45.dp, 100.dp, 300.dp, 78.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            StatBox(stringResource(R.string.label_time), formatElapsed(uiState.elapsedSeconds), Modifier.weight(1f).fillMaxSize())
            StatBox(stringResource(R.string.label_moves), uiState.moves.toString(), Modifier.weight(1f).fillMaxSize())
        }

        Image(
            painter = painterResource(R.drawable.board),
            contentDescription = null,
            modifier = Modifier.place(15.dp, 198.dp, 360.dp, 370.dp),
            contentScale = ContentScale.FillBounds,
        )
        uiState.board?.let { board ->
            GameBoard(
                board = board,
                round = uiState.round,
                enabled = uiState.acceptsMoves,
                onTileClick = { onEvent(GameContract.GameEvent.TileClicked(it)) },
                modifier = Modifier.offset(39.dp, 222.dp),
            )
        }
        Image(
            painter = painterResource(R.drawable.liana_l),
            contentDescription = null,
            modifier = Modifier.place((-8).dp, 172.dp, 82.dp, 116.dp).swing(halfCycleMillis = 3400, pivotX = 0f),
            contentScale = ContentScale.FillBounds,
        )
        Image(
            painter = painterResource(R.drawable.liana_r),
            contentDescription = null,
            modifier = Modifier.place(324.dp, 176.dp, 72.dp, 113.dp).swing(degrees = -3f, halfCycleMillis = 3900, pivotX = 1f),
            contentScale = ContentScale.FillBounds,
        )

        ImageIconButton(
            icon = R.drawable.btn_restart,
            contentDescription = stringResource(R.string.cd_shuffle),
            onClick = { onEvent(GameContract.GameEvent.ShuffleClicked) },
            modifier = Modifier.place(157.dp, 606.dp, 76.dp, 75.dp),
        )
    }
}

@Composable
private fun PauseOverlay(onResume: () -> Unit, onHome: () -> Unit, onSettings: () -> Unit) {
    ArtboardOverlay { PauseControls(onResume, onHome, onSettings) }
}

@Composable
private fun PauseControls(onResume: () -> Unit, onHome: () -> Unit, onSettings: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterVertically),
    ) {
        OutlinedText(
            text = stringResource(R.string.game_paused),
            modifier = Modifier.popIn(),
            fontSize = 52.sp,
            outlineWidth = 3.dp,
            shadowOffset = 6.dp,
        )
        WoodButton(stringResource(R.string.game_resume), onResume, Modifier.size(210.dp, 94.dp).pulse())
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            ImageIconButton(R.drawable.btn_home, stringResource(R.string.cd_home), onHome, Modifier.size(70.dp))
            ImageIconButton(R.drawable.btn_settings, stringResource(R.string.cd_settings), onSettings, Modifier.size(70.dp))
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun GameScreenPreview() {
    Puzzle15Theme {
        GameContent(
            uiState = GameContract.GameUiState(board = GameEngine().newBoard(BoardSize.FOUR), moves = 12, elapsedSeconds = 58),
            onEventDispatcher = {},
        )
    }
}
