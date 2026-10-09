package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.swing

/**
 * Hanging wooden sign with the screen title (WELCOME!, RECORDS, SETTINGS...).
 * The text box keeps the artwork's proportions at any size, so callers only set width × height.
 */
@Composable
fun SignHeader(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 32.sp,
) {
    BoxWithConstraints(modifier.swing()) {
        Image(
            painter = painterResource(R.drawable.sign_plank),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Box(
            modifier = Modifier
                .offset(x = maxWidth * 0.1667f, y = maxHeight * 0.36f)
                .size(width = maxWidth * 0.6667f, height = maxHeight * 0.29f)
                .semantics { heading() },
            contentAlignment = Alignment.Center,
        ) {
            OutlinedText(text = title, fontSize = fontSize, maxLines = 1)
        }
    }
}
