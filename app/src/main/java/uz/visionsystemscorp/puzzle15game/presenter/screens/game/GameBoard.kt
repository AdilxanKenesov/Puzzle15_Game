package uz.visionsystemscorp.puzzle15game.presenter.screens.game

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.domain.model.Board
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.pressClickable
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BarkDeep
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Butter
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Cream
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Rust

private val BoardSide = 312.dp
private val TileGap = 6.dp

@Composable
fun GameBoard(
    board: Board,
    round: Int,
    enabled: Boolean,
    onTileClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimension = board.size.dimension
    val tileSize = (BoardSide - TileGap * (dimension - 1)) / dimension
    val step = with(LocalDensity.current) { (tileSize + TileGap).toPx() }

    Box(modifier.size(BoardSide)) {
        key(round, dimension) {
            for (number in 1 until board.size.cellCount) {
                key(number) {
                    val index = board.tiles.indexOf(number)
                    val target = IntOffset(
                        x = ((index % dimension) * step).toInt(),
                        y = ((index / dimension) * step).toInt(),
                    )
                    val offset by animateIntOffsetAsState(
                        targetValue = target,
                        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
                        label = "tile",
                    )
                    Tile(
                        number = number,
                        isInPlace = board.isInPlace(index),
                        size = tileSize,
                        enabled = enabled,
                        onClick = { onTileClick(index) },
                        modifier = Modifier
                            .offset { offset }
                            .popIn(delayMillis = number * 30, initialScale = 0f, initialRotation = -25f),
                    )
                }
            }
        }
    }
}

@Composable
private fun Tile(
    number: Int,
    isInPlace: Boolean,
    size: Dp,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.cd_tile, number)
    Box(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = description }
            .pressClickable(enabled = enabled, playSound = false, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(if (isInPlace) R.drawable.tile_ok else R.drawable.tile),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        OutlinedText(
            text = number.toString(),
            modifier = Modifier.padding(bottom = size * 0.06f),
            fontSize = (size.value * 0.46f).sp,
            color = if (isInPlace) Rust else Cream,
            outlineColor = if (isInPlace) null else Bark,
            shadowColor = if (isInPlace) Butter else BarkDeep,
            shadowOffset = if (isInPlace) 3.dp else 4.dp,
            letterSpacing = 0.sp,
        )
    }
}
