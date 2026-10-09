package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.JungleScrim

/** Butun oynani (tizim panellari ham) qoraytiradi, ostidagi ekranga bosishni to'sadi. */
@Composable
fun ArtboardOverlay(content: @Composable BoxScope.() -> Unit) {
    DesignFrame(
        modifier = Modifier
            .fillMaxSize()
            .background(JungleScrim.copy(alpha = 0.65f))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
            .safeDrawingPadding(),
        content = content,
    )
}
