package uz.visionsystemscorp.puzzle15game.presenter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val ColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Bark,
    background = Jungle,
    onBackground = Cream,
    surface = Jungle,
    onSurface = Cream,
)

/**
 * Text sits on fixed-size PNG artwork, so the system font scale is pinned to 1
 * to keep labels inside their planks.
 */
@Composable
fun Puzzle15Theme(content: @Composable () -> Unit) {
    val density = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 1f)) {
        MaterialTheme(colorScheme = ColorScheme, content = content)
    }
}
