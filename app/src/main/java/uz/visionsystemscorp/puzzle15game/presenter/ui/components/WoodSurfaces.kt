package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Ember
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Peach

/** Slice insets of plank.png, in source pixels (CSS `border-image: url(plank) 80 100`). */
private const val PlankSliceX = 100
private const val PlankSliceY = 80

/**
 * Stretchable wooden plank: corners keep their shape, edges and centre stretch.
 * Same as the CSS `.wood` class, so any size panel can reuse plank.png.
 */
fun Modifier.plankBackground(borderX: Dp = 30.dp, borderY: Dp = 26.dp): Modifier = composed {
    val image = ImageBitmap.imageResource(R.drawable.plank)
    drawBehind { drawNineSlice(image, PlankSliceX, PlankSliceY, borderX.toPx(), borderY.toPx()) }
}

private fun DrawScope.drawNineSlice(
    image: ImageBitmap,
    sliceX: Int,
    sliceY: Int,
    borderX: Float,
    borderY: Float,
) {
    val srcX = intArrayOf(0, sliceX, image.width - sliceX, image.width)
    val srcY = intArrayOf(0, sliceY, image.height - sliceY, image.height)
    val dstX = intArrayOf(0, borderX.roundToInt(), (size.width - borderX).roundToInt(), size.width.roundToInt())
    val dstY = intArrayOf(0, borderY.roundToInt(), (size.height - borderY).roundToInt(), size.height.roundToInt())
    for (row in 0..2) {
        for (col in 0..2) {
            drawImage(
                image = image,
                srcOffset = IntOffset(srcX[col], srcY[row]),
                srcSize = IntSize(srcX[col + 1] - srcX[col], srcY[row + 1] - srcY[row]),
                dstOffset = IntOffset(dstX[col], dstY[row]),
                dstSize = IntSize(dstX[col + 1] - dstX[col], dstY[row + 1] - dstY[row]),
                filterQuality = FilterQuality.Medium,
            )
        }
    }
}

/** Wooden plank panel; content is inset by the plank border like the CSS `.wood` box. */
@Composable
fun WoodPanel(
    modifier: Modifier = Modifier,
    borderX: Dp = 30.dp,
    borderY: Dp = 26.dp,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .plankBackground(borderX, borderY)
            .padding(horizontal = borderX, vertical = borderY),
        contentAlignment = contentAlignment,
        content = content,
    )
}

/** Recessed dark-wood well used for stat cards and the name input. */
@Composable
fun InsetBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp,
    borderWidth: Dp = 3.dp,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .drawBehind {
                // Light ledge under the well (CSS `box-shadow: 0 2px 0 #F2944E`).
                drawRoundRect(
                    color = Peach,
                    topLeft = Offset(0f, 2.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            }
            .background(Ember, shape)
            .border(borderWidth, Bark, shape),
        contentAlignment = contentAlignment,
        content = content,
    )
}

/** CSS `filter: saturate(s) brightness(b)` for dimmed / highlighted artwork. */
fun woodTint(saturation: Float, brightness: Float): ColorFilter =
    ColorFilter.colorMatrix(
        ColorMatrix().apply {
            setToSaturation(saturation)
            timesAssign(ColorMatrix().apply { setToScale(brightness, brightness, brightness, 1f) })
        },
    )
