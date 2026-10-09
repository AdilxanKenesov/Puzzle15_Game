package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.R

private val DisabledTint = woodTint(saturation = 0.3f, brightness = 0.7f)

/** Text button on plank.png (SAVE, RESUME, REPLAY, GOOGLE...). The caller sets its size. */
@Composable
fun WoodButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
    tint: ColorFilter? = null,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    Box(modifier.pressClickable(enabled = enabled, onClick = onClick), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.plank),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds,
            colorFilter = if (enabled) tint else DisabledTint,
        )
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()
            OutlinedText(text = text, fontSize = fontSize, maxLines = 1)
        }
    }
}
