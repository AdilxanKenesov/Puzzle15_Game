package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.visionsystemscorp.puzzle15game.R

@Composable
fun TopSign(title: String, fontSize: TextUnit) {
    SignHeader(title, Modifier.place(75.dp, (-14).dp, 240.dp, 178.dp), fontSize)
}

@Composable
fun TopLeftHomeButton(onClick: () -> Unit) {
    ImageIconButton(
        icon = R.drawable.btn_home,
        contentDescription = stringResource(R.string.cd_home),
        onClick = onClick,
        modifier = Modifier.place(14.dp, 20.dp, 58.dp, 58.dp),
    )
}

@Composable
fun TopRightCloseButton(onClick: () -> Unit) {
    ImageIconButton(
        icon = R.drawable.btn_close,
        contentDescription = stringResource(R.string.cd_close),
        onClick = onClick,
        modifier = Modifier.place(DesignWidth - 14.dp - 58.dp, 20.dp, 58.dp, 57.dp),
    )
}

@Composable
fun BoardPanel(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 80.dp,
    topPadding: Dp = 40.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier) {
        Image(
            painter = painterResource(R.drawable.panel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Box(
            modifier = Modifier.padding(start = horizontalPadding, end = horizontalPadding, top = topPadding),
            content = content,
        )
    }
}
