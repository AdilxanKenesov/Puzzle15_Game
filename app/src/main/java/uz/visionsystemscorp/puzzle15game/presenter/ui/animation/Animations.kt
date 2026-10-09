package uz.visionsystemscorp.puzzle15game.presenter.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Ports of the CSS keyframes in the design screens. Animated values are read
// inside graphicsLayer {} so they only redraw the layer, never recompose.

/** Endless 0 → 1 → 0 ping-pong, eased like CSS `ease-in-out ... infinite alternate`. */
private fun Modifier.pingPong(
    label: String,
    halfCycleMillis: Int,
    delayMillis: Int,
    block: GraphicsLayerScope.(Float) -> Unit,
): Modifier = composed {
    val progress by rememberInfiniteTransition(label).animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(halfCycleMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delayMillis, StartOffsetType.FastForward),
        ),
        label = label,
    )
    graphicsLayer { block(progress) }
}

/** Floating island: moves up by [distance] and back. */
fun Modifier.floaty(distance: Dp = 14.dp, periodMillis: Int = 4200, delayMillis: Int = 0): Modifier =
    pingPong("floaty", periodMillis / 2, delayMillis) { translationY = -distance.toPx() * it }

/**
 * Hanging sign or liana: rocks ±[degrees] around a pivot on its top edge
 * ([pivotX] 0 = left, 1 = right). Negative [degrees] starts the swing the other way.
 */
fun Modifier.swing(degrees: Float = 3f, halfCycleMillis: Int = 2600, pivotX: Float = 0.5f): Modifier =
    pingPong("swing", halfCycleMillis, 0) {
        transformOrigin = TransformOrigin(pivotX, 0f)
        rotationZ = -degrees + 2 * degrees * it
    }

/** Logo wobble: tilts ±2° while growing slightly. */
fun Modifier.wobble(periodMillis: Int = 3200): Modifier =
    pingPong("wobble", periodMillis / 2, 0) {
        rotationZ = -2f + 4f * it
        val scale = 1f + 0.03f * it
        scaleX = scale
        scaleY = scale
    }

/** Call-to-action breathing (PLAY, SAVE, REPLAY). */
fun Modifier.pulse(maxScale: Float = 1.07f, periodMillis: Int = 1500): Modifier =
    pingPong("pulse", periodMillis / 2, 0) {
        val scale = 1f + (maxScale - 1f) * it
        scaleX = scale
        scaleY = scale
    }

/** Blinking "..." after LOADING. */
fun Modifier.blink(periodMillis: Int = 1200): Modifier =
    pingPong("blink", periodMillis / 2, 0) { alpha = 0.3f + 0.7f * it }

/** Arrow hint: slides horizontally by [distance] (negative = left). */
fun Modifier.nudgeX(distance: Dp, periodMillis: Int = 1200): Modifier =
    pingPong("nudge", periodMillis / 2, 0) { translationX = distance.toPx() * it }

/**
 * One-shot bouncy scale-in for dialogs, panels, tiles and stars.
 * [initialRotation] spins it into place (stars); [initialScale] > 1 stamps it down (NEW BEST).
 */
fun Modifier.popIn(
    delayMillis: Int = 0,
    initialScale: Float = 0.2f,
    initialRotation: Float = 0f,
): Modifier = composed {
    val progress = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        launch { alpha.animateTo(1f, tween(200)) }
        progress.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMediumLow))
    }
    graphicsLayer {
        val scale = initialScale + (1f - initialScale) * progress.value
        scaleX = scale
        scaleY = scale
        rotationZ = initialRotation * (1f - progress.value)
        this.alpha = alpha.value
    }
}

/** One-shot drop from above with a bounce (hanging signs on Login / Name / Win). */
fun Modifier.dropIn(delayMillis: Int = 0, fromHeight: Dp = 560.dp): Modifier = composed {
    val offset = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        offset.animateTo(0f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
    }
    graphicsLayer { translationY = -fromHeight.toPx() * offset.value }
}

/** One-shot slide + fade in: bottom button rows rise up, table rows slide in from the left. */
fun Modifier.slideIn(delayMillis: Int = 0, offsetX: Dp = 0.dp, offsetY: Dp = 60.dp, durationMillis: Int = 500): Modifier =
    composed {
        val progress = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            delay(delayMillis.toLong())
            progress.animateTo(1f, tween(durationMillis))
        }
        graphicsLayer {
            translationX = offsetX.toPx() * (1f - progress.value)
            translationY = offsetY.toPx() * (1f - progress.value)
            alpha = progress.value
        }
    }
