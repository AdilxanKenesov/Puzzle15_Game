package uz.visionsystemscorp.puzzle15game.presenter.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.delay
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.blink
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.dropIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.floaty
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BarkDeep
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Ember
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Gold
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme

private const val LoadingMillis = 2100
private const val HoldMillis = 300L

@Composable
fun SplashScreen() {
    val viewModel: SplashContract.SplashViewModel = hiltViewModel<SplashViewModel>()
    SplashContent(onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
internal fun SplashContent(onEventDispatcher: (SplashContract.SplashEvent) -> Unit) {
    val currentOnEvent by rememberUpdatedState(onEventDispatcher)
    val progress = remember { Animatable(0.04f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(LoadingMillis, easing = FastOutSlowInEasing))
        delay(HoldMillis)
        currentOnEvent(SplashContract.SplashEvent.LoadingFinished)
    }

    ScreenBackground(background = R.drawable.bg_splash, scrimAlpha = 0.08f, showFireflies = true) {
        // The title is painted on the hanging jungle sign, which drops in on its ropes.
        Box(
            modifier = Modifier
                .place(0.dp, 0.dp, 390.dp, 327.dp)
                .dropIn(),
        ) {
            Image(
                painter = painterResource(R.drawable.sign_hanging),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
            Column(
                modifier = Modifier.place(70.dp, 172.dp, 262.dp, 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                OutlinedText(
                    text = stringResource(R.string.splash_title_top),
                    fontSize = 40.sp,
                    letterSpacing = 3.sp,
                    outlineWidth = 3.dp,
                    shadowOffset = 4.dp,
                )
                OutlinedText(
                    text = stringResource(R.string.splash_title_bottom),
                    modifier = Modifier.offset(y = (-6).dp),
                    fontSize = 64.sp,
                    color = Gold,
                    outlineWidth = 3.dp,
                    shadowOffset = 6.dp,
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.island_palm),
            contentDescription = null,
            modifier = Modifier
                .place(75.dp, 372.dp, 240.dp, 281.dp)
                .popIn(delayMillis = 300, initialScale = 0.4f)
                .floaty(distance = 16.dp, periodMillis = 4000),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            modifier = Modifier.place(55.dp, 690.dp, 280.dp, 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LoadingBar(progress.value, Modifier.size(280.dp, 28.dp))
            Row {
                OutlinedText(stringResource(R.string.splash_loading), fontSize = 22.sp, letterSpacing = 2.sp, shadowColor = null)
                OutlinedText("...", Modifier.blink(), fontSize = 22.sp, letterSpacing = 2.sp, shadowColor = null)
            }
        }
    }
}

@Composable
private fun LoadingBar(progress: Float, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier
            .drawBehind {
                drawRoundRect(BarkDeep, Offset(0f, 4.dp.toPx()), size, CornerRadius(14.dp.toPx()))
            }
            .clip(shape)
            .background(Ember)
            .border(4.dp, Bark, shape)
            .padding(4.dp),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(RoundedCornerShape(8.dp))
                .background(Gold),
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    Puzzle15Theme { SplashContent(onEventDispatcher = {}) }
}
