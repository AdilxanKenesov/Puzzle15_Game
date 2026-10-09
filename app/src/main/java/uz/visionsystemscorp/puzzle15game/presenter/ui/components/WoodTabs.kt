package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.visionsystemscorp.puzzle15game.R

private val SelectedTint = woodTint(saturation = 1f, brightness = 1.12f)
private val IdleTint = woodTint(saturation = 0.6f, brightness = 0.8f)

@Composable
fun <T> WoodTabs(
    items: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: @Composable (T) -> String,
    tabSize: DpSize,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
) {
    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
    ) {
        items.forEach { item ->
            val isSelected = item == selected
            Box(
                modifier = Modifier
                    .size(tabSize)
                    .graphicsLayer { translationY = if (isSelected) -4.dp.toPx() else 0f }
                    .semantics { this.selected = isSelected }
                    .pressClickable(role = Role.Tab) { onSelect(item) },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.plank),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = if (isSelected) SelectedTint else IdleTint,
                )
                OutlinedText(
                    text = label(item),
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontSize = fontSize,
                    shadowColor = null,
                    maxLines = 1,
                )
            }
        }
    }
}
