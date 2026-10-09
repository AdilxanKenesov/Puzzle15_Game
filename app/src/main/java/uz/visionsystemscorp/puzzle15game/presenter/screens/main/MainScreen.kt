package uz.visionsystemscorp.puzzle15game.presenter.screens.main

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.floaty
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.pulse
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.swing
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.wobble
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.BoardSizeSelector
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.DesignWidth
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ImageIconButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.GoldLight
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Maroon
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Peach
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme

@Composable
fun MainScreen() {
    val viewModel: MainContract.MainViewModel = hiltViewModel<MainViewModel>()
    val uiState = viewModel.collectAsState()
    MainContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
internal fun MainContent(
    uiState: MainContract.MainUiState,
    onEventDispatcher: (MainContract.MainEvent) -> Unit,
) {
    ScreenBackground(showFireflies = true) {
        FloatingIslands()

        OutlinedText(
            text = stringResource(R.string.main_logo),
            modifier = Modifier.offset(y = 178.dp).fillMaxWidth().wobble(),
            fontSize = 56.sp,
            letterSpacing = 2.sp,
            outlineWidth = 3.dp,
            shadowOffset = 6.dp,
        )

        GreetingSign(uiState.playerName, Modifier.place(6.dp, (-6).dp, 168.dp, 164.dp))

        ImageIconButton(
            icon = R.drawable.btn_settings,
            contentDescription = stringResource(R.string.cd_settings),
            onClick = { onEventDispatcher(MainContract.MainEvent.SettingsClicked) },
            modifier = Modifier.place(DesignWidth - 12.dp - 60.dp, 20.dp, 60.dp, 60.dp),
        )

        BoardSizeSelector(
            size = uiState.boardSize,
            hasPrevious = uiState.hasSmallerSize,
            hasNext = uiState.hasBiggerSize,
            onPrevious = { onEventDispatcher(MainContract.MainEvent.PreviousSizeClicked) },
            onNext = { onEventDispatcher(MainContract.MainEvent.NextSizeClicked) },
            modifier = Modifier.offset(y = 556.dp).fillMaxWidth(),
        )

        Row(
            modifier = Modifier.offset(y = 690.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageIconButton(
                icon = R.drawable.btn_list,
                contentDescription = stringResource(R.string.cd_records),
                onClick = { onEventDispatcher(MainContract.MainEvent.RecordsClicked) },
                modifier = Modifier.size(74.dp, 73.dp),
            )
            ImageIconButton(
                icon = R.drawable.btn_play_txt,
                contentDescription = stringResource(R.string.cd_play),
                onClick = { onEventDispatcher(MainContract.MainEvent.PlayClicked) },
                modifier = Modifier.size(210.dp, 103.dp).pulse(),
            )
            ImageIconButton(
                icon = R.drawable.btn_blank,
                contentDescription = stringResource(R.string.cd_about),
                onClick = { onEventDispatcher(MainContract.MainEvent.InfoClicked) },
                modifier = Modifier.size(74.dp),
            ) {
                OutlinedText(
                    text = "i",
                    modifier = Modifier.offset(y = (-4).dp),
                    fontSize = 38.sp,
                    color = Maroon,
                    outlineColor = null,
                    shadowColor = Peach,
                    shadowOffset = 3.dp,
                )
            }
        }
    }
}

@Composable
private fun FloatingIslands() {
    Image(
        painter = painterResource(R.drawable.island_fall),
        contentDescription = null,
        modifier = Modifier.place(4.dp, 300.dp, 104.dp, 72.dp).floaty(periodMillis = 5600, delayMillis = 2000).alpha(0.92f),
        contentScale = ContentScale.FillBounds,
    )
    Image(
        painter = painterResource(R.drawable.island_pine),
        contentDescription = null,
        modifier = Modifier.place(308.dp, 270.dp, 68.dp, 84.dp).floaty(periodMillis = 6400, delayMillis = 3500).alpha(0.9f),
        contentScale = ContentScale.FillBounds,
    )
    Image(
        painter = painterResource(R.drawable.island_palm),
        contentDescription = null,
        modifier = Modifier.place(80.dp, 256.dp, 230.dp, 269.dp).floaty(),
        contentScale = ContentScale.FillBounds,
    )
}

/** Round sign in the top-left corner: "Hi, <name>!". */
@Composable
private fun GreetingSign(name: String, modifier: Modifier = Modifier) {
    Box(modifier.swing(degrees = 4f)) {
        Image(
            painter = painterResource(R.drawable.sign_round),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        // Centred on the wooden plate between the ropes, in the game's cream-on-bark title style.
        Column(
            modifier = Modifier.place(30.dp, 40.dp, 110.dp, 58.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedText(
                text = stringResource(R.string.main_greeting),
                fontSize = 15.sp,
                shadowOffset = 2.dp,
                letterSpacing = 0.sp,
                maxLines = 1,
            )
            OutlinedText(
                text = stringResource(R.string.main_greeting_name, name),
                // Long names shrink so they stay on the plate.
                fontSize = if (name.length > 9) 14.sp else 19.sp,
                color = GoldLight,
                shadowOffset = 2.dp,
                letterSpacing = 0.sp,
                maxLines = 1,
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun MainScreenPreview() {
    Puzzle15Theme {
        MainContent(uiState = MainContract.MainUiState(playerName = "Adilxan"), onEventDispatcher = {})
    }
}
