package uz.visionsystemscorp.puzzle15game.presenter.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import uz.visionsystemscorp.puzzle15game.R

/** Round PNG button (home, settings, pause, shuffle...). The caller sets its size. */
@Composable
fun ImageIconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .semantics { this.contentDescription = contentDescription }
            .pressClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds,
        )
        overlay?.invoke(this)
    }
}

/** Sound on/off toggle shared by Main and Game. */
@Composable
fun SoundButton(isOn: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    ImageIconButton(
        icon = if (isOn) R.drawable.btn_sound_on else R.drawable.btn_sound_off,
        contentDescription = stringResource(if (isOn) R.string.cd_sound_on else R.string.cd_sound_off),
        onClick = onToggle,
        modifier = modifier,
    )
}
