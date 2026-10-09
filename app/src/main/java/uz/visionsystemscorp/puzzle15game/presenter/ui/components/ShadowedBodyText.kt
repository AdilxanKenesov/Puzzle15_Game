package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BodyStyle

/** Paragraph text with the 2 dp hard shadow used on wooden boards. */
@Composable
fun ShadowedBodyText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = BodyStyle,
    textAlign: TextAlign = TextAlign.Center,
) {
    val offset = with(LocalDensity.current) { 2.dp.toPx() }
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(shadow = Shadow(Bark, Offset(0f, offset), blurRadius = 0f)),
        textAlign = textAlign,
    )
}
