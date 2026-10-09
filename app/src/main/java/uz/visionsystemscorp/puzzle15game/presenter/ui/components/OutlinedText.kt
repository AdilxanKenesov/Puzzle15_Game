package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BarkDeep
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Cream
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LuckiestGuy

@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    color: Color = Cream,
    outlineColor: Color? = Bark,
    outlineWidth: Dp = 2.dp,
    shadowColor: Color? = BarkDeep,
    shadowOffset: Dp = 4.dp,
    letterSpacing: TextUnit = 1.sp,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = Int.MAX_VALUE,
) {
    val density = LocalDensity.current
    val shadow = shadowColor?.let {
        Shadow(it, Offset(0f, with(density) { shadowOffset.toPx() }), blurRadius = 0f)
    }
    val base = TextStyle(
        fontFamily = LuckiestGuy,
        fontSize = fontSize,
        lineHeight = fontSize * 1.05f,
        letterSpacing = letterSpacing,
        textAlign = textAlign,
    )
    Box(modifier, contentAlignment = Alignment.Center) {
        if (outlineColor != null) {
            Text(
                text = text,
                modifier = Modifier.clearAndSetSemantics {},
                style = base.copy(
                    color = outlineColor,
                    shadow = shadow,
                    drawStyle = Stroke(
                        width = with(density) { outlineWidth.toPx() } * 2,
                        join = StrokeJoin.Round,
                    ),
                ),
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = text,
            style = base.copy(color = color, shadow = if (outlineColor == null) shadow else null),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
