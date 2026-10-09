package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.domain.model.BoardSize
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.nudgeX
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.difficultyLabel
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.label

/** "4×4 / CLASSIC" badge on a plank; shared by the Main selector and the Game header. */
@Composable
fun BoardSizeBadge(
    size: BoardSize,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    labelSize: TextUnit = 11.sp,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.plank),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            OutlinedText(size.label(), fontSize = fontSize, shadowOffset = 3.dp, maxLines = 1)
            Text(size.difficultyLabel(), style = LabelStyle.copy(fontSize = labelSize))
        }
    }
}

/** ◀ 4×4 ▶ picker on the Main screen. An arrow with nowhere to go is hidden but keeps its space. */
@Composable
fun BoardSizeSelector(
    size: BoardSize,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    spacing: Dp = 6.dp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val arrowSize = Modifier.size(60.dp, 52.dp)
        if (hasPrevious) {
            ImageIconButton(
                icon = R.drawable.btn_prev,
                contentDescription = stringResource(R.string.cd_smaller_board),
                onClick = onPrevious,
                modifier = arrowSize.nudgeX((-5).dp),
            )
        } else {
            Spacer(arrowSize)
        }
        BoardSizeBadge(size, Modifier.size(164.dp, 74.dp))
        if (hasNext) {
            ImageIconButton(
                icon = R.drawable.btn_next,
                contentDescription = stringResource(R.string.cd_bigger_board),
                onClick = onNext,
                modifier = arrowSize.nudgeX(5.dp),
            )
        } else {
            Spacer(arrowSize)
        }
    }
}
