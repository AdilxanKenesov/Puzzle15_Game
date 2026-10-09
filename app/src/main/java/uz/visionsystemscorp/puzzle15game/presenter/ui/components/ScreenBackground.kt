package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.Fireflies
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Jungle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.JungleScrim

/**
 * Shared root of every screen: full-bleed jungle art, an optional dark scrim and
 * the scaled [DesignFrame] that [content] is placed on.
 *
 * @param background full-bleed art behind the artboard.
 * @param overlay drawn above everything across the whole window (not just the artboard),
 * e.g. a dialog whose scrim must also cover the system bars and letterbox areas.
 */
@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    @DrawableRes background: Int = R.drawable.bg_main,
    scrimAlpha: Float = 0f,
    showFireflies: Boolean = false,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize().background(Jungle)) {
        Image(
            painter = painterResource(background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        if (scrimAlpha > 0f) {
            Box(Modifier.fillMaxSize().background(JungleScrim.copy(alpha = scrimAlpha)))
        }
        DesignFrame(Modifier.fillMaxSize().safeDrawingPadding()) {
            if (showFireflies) Fireflies()
            content()
        }
        overlay?.invoke(this)
    }
}
