package uz.visionsystemscorp.puzzle15game.presenter.screens.win

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.navigation.WinRoute
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.domain.model.GameResult
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.FallingLeaves
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.dropIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.pulse
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.slideIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.swing
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.BoardPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ImageIconButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.StatBox
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.StatBoxStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Leaf
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LeafDark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.formatElapsed
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.label

private const val PanelDropDelay = 250
private const val StarsDelay = 1000
private const val NumbersDelay = 1700
private const val ButtonsDelay = 1900
private const val StampDelay = 2100

@Composable
fun WinScreen(route: WinRoute) {
    val viewModel: WinContract.WinViewModel =
        hiltViewModel<WinViewModel, WinViewModel.Factory>(creationCallback = { it.create(route) })
    val uiState = viewModel.collectAsState()
    WinContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
private fun WinContent(
    uiState: WinContract.WinUiState,
    onEventDispatcher: (WinContract.WinEvent) -> Unit,
) {
    val result = uiState.result
    ScreenBackground(scrimAlpha = 0.42f) {
        FallingLeaves()

        Image(
            painter = painterResource(R.drawable.sign_victory),
            contentDescription = stringResource(R.string.cd_victory),
            modifier = Modifier.place(60.dp, 50.dp, 270.dp, 227.dp).dropIn().swing(),
            contentScale = ContentScale.FillBounds,
        )

        BoardPanel(
            modifier = Modifier.place(5.dp, 296.dp, 380.dp, 285.dp).dropIn(delayMillis = PanelDropDelay),
            horizontalPadding = 80.dp,
            topPadding = 24.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Stars(result.stars)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val numbers = Modifier.weight(1f).popIn(delayMillis = NumbersDelay, initialScale = 2f)
                    StatBox(stringResource(R.string.label_time), formatElapsed(result.seconds), numbers, StatBoxStyle.Inset)
                    StatBox(stringResource(R.string.label_moves), result.moves.toString(), numbers, StatBoxStyle.Inset)
                }
                Text(
                    text = stringResource(R.string.win_star_hint, result.size.label(), result.size.parMoves),
                    style = LabelStyle.copy(letterSpacing = 1.sp),
                    textAlign = TextAlign.Center,
                )
            }
        }

        if (result.isNewBest) {
            NewBestStamp(Modifier.place(295.dp, 312.dp, 76.dp, 76.dp).popIn(delayMillis = StampDelay, initialScale = 2.4f))
        }

        Row(
            modifier = Modifier.offset(y = 640.dp).fillMaxWidth().slideIn(delayMillis = ButtonsDelay),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageIconButton(R.drawable.btn_home, stringResource(R.string.cd_home), { onEventDispatcher(WinContract.WinEvent.HomeClicked) }, Modifier.size(74.dp))
            WoodButton(stringResource(R.string.win_replay), { onEventDispatcher(WinContract.WinEvent.ReplayClicked) }, Modifier.size(210.dp, 94.dp).pulse())
            ImageIconButton(R.drawable.btn_list, stringResource(R.string.cd_records), { onEventDispatcher(WinContract.WinEvent.RecordsClicked) }, Modifier.size(74.dp, 73.dp))
        }
    }
}

/** Three stars, the middle one larger and raised; each spins in after the panel lands. */
@Composable
private fun Stars(count: Int) {
    val description = stringResource(R.string.cd_stars, count)
    Row(
        modifier = Modifier.height(82.dp).clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        repeat(3) { i ->
            val isCenter = i == 1
            Image(
                painter = painterResource(if (i < count) R.drawable.ic_star_full else R.drawable.ic_star_empty),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = if (isCenter) 12.dp else 0.dp)
                    .size(if (isCenter) 80.dp else 62.dp)
                    .popIn(delayMillis = StarsDelay + i * 200, initialScale = 0f, initialRotation = -140f),
            )
        }
    }
}

@Composable
private fun NewBestStamp(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .rotate(14f)
            .drawBehind { drawCircle(LeafDark, center = center + Offset(0f, 4.dp.toPx())) }
            .background(Leaf, CircleShape)
            .border(4.dp, LeafDark, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        OutlinedText(
            text = stringResource(R.string.win_new_best),
            fontSize = 16.sp,
            color = Color.White,
            outlineColor = null,
            shadowColor = LeafDark,
            shadowOffset = 2.dp,
            letterSpacing = 0.sp,
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun WinScreenPreview() {
    Puzzle15Theme {
        WinContent(
            uiState = WinContract.WinUiState(GameResult(BoardSize.FOUR, moves = 130, seconds = 58, stars = 2, isNewBest = true)),
            onEventDispatcher = {},
        )
    }
}
