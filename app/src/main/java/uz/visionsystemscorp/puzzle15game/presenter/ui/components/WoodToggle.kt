package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BarkDeep
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Cream
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Ember
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Leaf
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LeafDark
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.LocalClickSound

/** Green / brown pill switch on the Settings screen (74×38 dp). */
@Composable
fun WoodToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val knobOffset by animateDpAsState(
        targetValue = if (checked) 34.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "knob",
    )
    val fill by animateColorAsState(if (checked) Leaf else Ember, label = "fill")
    val edge by animateColorAsState(if (checked) LeafDark else BarkDeep, label = "edge")
    val shape = RoundedCornerShape(19.dp)
    val clickSound = LocalClickSound.current
    Box(
        modifier = modifier
            .size(74.dp, 38.dp)
            .drawBehind {
                drawRoundRect(edge, Offset(0f, 3.dp.toPx()), size, CornerRadius(19.dp.toPx()))
            }
            .background(fill, shape)
            .border(4.dp, edge, shape)
            .semantics { this.contentDescription = contentDescription }
            .toggleable(value = checked, role = Role.Switch) {
                clickSound()
                onCheckedChange(it)
            }
            .padding(7.dp),
    ) {
        Box(
            Modifier
                .offset { IntOffset(knobOffset.roundToPx(), 0) }
                .size(24.dp)
                .drawBehind {
                    drawCircle(Bark, center = center.copy(y = center.y + 2.dp.toPx()))
                }
                .background(Cream, CircleShape),
        )
    }
}
