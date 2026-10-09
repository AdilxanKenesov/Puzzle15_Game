package uz.visionsystemscorp.puzzle15game.presenter.ui.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.FireflyCore
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.FireflyGlow
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.FireflyGlowCyan

private data class Firefly(
    val x: Dp,
    val y: Dp,
    val size: Dp,
    val core: Color,
    val glow: Color,
    val delayMillis: Int,
)

private val Swarm = listOf(
    Firefly(60.dp, 520.dp, 7.dp, FireflyCore, FireflyGlow, 0),
    Firefly(330.dp, 470.dp, 6.dp, FireflyCore, FireflyGlow, 1100),
    Firefly(190.dp, 610.dp, 5.dp, Color.White, FireflyGlowCyan, 2000),
    Firefly(120.dp, 420.dp, 5.dp, FireflyCore, FireflyGlow, 500),
)

@Composable
fun Fireflies(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition("fireflies")
    Box(modifier.fillMaxSize()) {
        Swarm.forEach { fly ->
            val glow by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1600, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(fly.delayMillis),
                ),
                label = "firefly",
            )
            Box(
                Modifier
                    .offset(fly.x, fly.y)
                    .size(fly.size)
                    .graphicsLayer {
                        val scale = 0.6f + 0.4f * glow
                        scaleX = scale
                        scaleY = scale
                        alpha = 0.1f + 0.9f * glow
                        translationX = 8.dp.toPx() * glow
                        translationY = -14.dp.toPx() * glow
                    }
                    .drawBehind {
                        val radius = size.minDimension / 2
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(fly.glow, Color.Transparent),
                                center = center,
                                radius = radius * 3.5f,
                            ),
                            radius = radius * 3.5f,
                        )
                        drawCircle(fly.core, radius)
                    },
            )
        }
    }
}
