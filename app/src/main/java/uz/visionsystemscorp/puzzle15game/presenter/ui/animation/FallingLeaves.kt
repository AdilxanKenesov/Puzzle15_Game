package uz.visionsystemscorp.puzzle15game.presenter.ui.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private data class Leaf(
    val x: Dp,
    val width: Dp,
    val fill: Color,
    val edge: Color,
    val durationMillis: Int,
    val delayMillis: Int,
)

private val Leaves = listOf(
    Leaf(30.dp, 26.dp, Color(0xFF5DBB3A), Color(0xFF1F5A1A), 7000, 1000),
    Leaf(120.dp, 22.dp, Color(0xFF8FD94A), Color(0xFF2E6A14), 8500, 3500),
    Leaf(230.dp, 26.dp, Color(0xFF3E9A2E), Color(0xFF1F5A1A), 7000, 5200),
    Leaf(330.dp, 20.dp, Color(0xFFFFC233), Color(0xFF7A3A00), 6200, 2200),
    Leaf(180.dp, 24.dp, Color(0xFF5DBB3A), Color(0xFF1F5A1A), 9000, 6400),
)

/** Leaf outline from the 26×22 SVG on the Win screen. */
private val LeafPath = Path().apply {
    moveTo(1f, 12f)
    quadraticTo(12f, -6f, 25f, 5f)
    quadraticTo(14f, 24f, 1f, 12f)
    close()
}

/** Leaves tumbling down the Win screen, in design-frame coordinates. */
@Composable
fun FallingLeaves(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition("leaves")
    Box(modifier.fillMaxSize()) {
        Leaves.forEach { leaf ->
            val fall by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(leaf.durationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                    // Negative CSS delay: start part-way through the fall.
                    initialStartOffset = StartOffset(leaf.delayMillis, StartOffsetType.FastForward),
                ),
                label = "leaf",
            )
            Canvas(
                Modifier
                    .offset(x = leaf.x)
                    .size(leaf.width, leaf.width * 22f / 26f)
                    .graphicsLayer {
                        translationY = (-60).dp.toPx() + 980.dp.toPx() * fall
                        rotationZ = 640f * fall
                    },
            ) {
                scale(size.width / 26f, pivot = Offset.Zero) {
                    drawPath(LeafPath, leaf.fill)
                    drawPath(LeafPath, leaf.edge, style = Stroke(width = 1.5f))
                }
            }
        }
    }
}
