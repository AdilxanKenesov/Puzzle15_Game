package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

/** Artboard size of every screen in the design screens. */
val DesignWidth = 390.dp
val DesignHeight = 844.dp

/**
 * Lays out [content] on a fixed 390×844 dp artboard and scales it to fit the
 * available space, so screens can use the exact dp values from the design.
 */
@Composable
fun DesignFrame(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val scale = min(maxWidth / DesignWidth, maxHeight / DesignHeight)
        val density = LocalDensity.current
        CompositionLocalProvider(
            LocalDensity provides Density(density.density * scale, density.fontScale),
        ) {
            Box(Modifier.size(DesignWidth, DesignHeight), content = content)
        }
    }
}

/** Absolute placement on the artboard, mirroring the design's `left/top/width/height`. */
fun Modifier.place(x: Dp, y: Dp, width: Dp, height: Dp): Modifier =
    offset(x, y).size(width, height)
