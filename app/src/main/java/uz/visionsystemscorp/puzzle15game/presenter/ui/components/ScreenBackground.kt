package uz.visionsystemscorp.puzzle15game.presenter.ui.components

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
 * Barcha ekranlarning asosi: fon rasmi, ixtiyoriy qoraytirish va [DesignFrame] maketi.
 * @param overlay butun oyna ustida chiziladi (masalan, tizim panellarini ham yopadigan dialog).
 */
@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    scrimAlpha: Float = 0f,
    showFireflies: Boolean = false,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize().background(Jungle)) {
        Image(
            painter = painterResource(R.drawable.bg_main),
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
