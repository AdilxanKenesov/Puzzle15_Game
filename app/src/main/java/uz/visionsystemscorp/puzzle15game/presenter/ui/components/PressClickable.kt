package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.LocalClickSound

fun Modifier.pressClickable(
    role: Role = Role.Button,
    enabled: Boolean = true,
    playSound: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    val clickSound = LocalClickSound.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.9f else 1f, tween(120), label = "press")
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        role = role,
        onClick = {
            if (playSound) clickSound()
            onClick()
        },
    )
}
