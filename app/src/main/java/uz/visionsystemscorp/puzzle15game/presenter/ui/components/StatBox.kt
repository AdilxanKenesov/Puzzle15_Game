package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle

enum class StatBoxStyle { Wood, Inset }

/** "TIME 0:58" / "MOVES 130" card: wooden on Game, recessed on Win and Records. */
@Composable
fun StatBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    style: StatBoxStyle = StatBoxStyle.Wood,
    valueFontSize: TextUnit = if (style == StatBoxStyle.Wood) 24.sp else 26.sp,
) {
    val content: @Composable () -> Unit = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(text = label, style = LabelStyle)
            when (style) {
                StatBoxStyle.Wood -> OutlinedText(value, fontSize = valueFontSize, shadowColor = null, maxLines = 1)
                StatBoxStyle.Inset -> OutlinedText(value, fontSize = valueFontSize, outlineColor = null, shadowOffset = 3.dp, maxLines = 1)
            }
        }
    }
    when (style) {
        StatBoxStyle.Wood -> WoodPanel(modifier, borderX = 24.dp, borderY = 20.dp, contentAlignment = Alignment.Center) { content() }
        StatBoxStyle.Inset -> InsetBox(modifier) { content() }
    }
}
