package uz.visionsystemscorp.puzzle15game.presenter.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.visionsystemscorp.puzzle15game.BuildConfig
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsContract.SettingsEvent
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopRightCloseButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopSign
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodToggle
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.pressClickable
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.GoldLight
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme

@Composable
fun SettingsScreen() {
    val viewModel: SettingsContract.SettingsViewModel = hiltViewModel<SettingsViewModel>()
    val uiState = viewModel.collectAsState()
    SettingsContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
private fun SettingsContent(
    uiState: SettingsContract.SettingsUiState,
    onEventDispatcher: (SettingsEvent) -> Unit,
) {
    ScreenBackground(scrimAlpha = 0.42f) {
        TopSign(stringResource(R.string.settings_title), fontSize = 26.sp)
        TopRightCloseButton { onEventDispatcher(SettingsEvent.CloseClicked) }

        WoodPanel(Modifier.place(15.dp, 190.dp, 360.dp, 450.dp).popIn(delayMillis = 150, initialScale = 0.3f)) {
            Column(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ToggleRow(stringResource(R.string.settings_music), uiState.settings.music) { onEventDispatcher(SettingsEvent.MusicChanged(it)) }
                ToggleRow(stringResource(R.string.settings_sound), uiState.settings.sound) { onEventDispatcher(SettingsEvent.SoundChanged(it)) }
                ToggleRow(stringResource(R.string.settings_vibration), uiState.settings.vibration) { onEventDispatcher(SettingsEvent.VibrationChanged(it)) }
                SettingRow(Modifier.pressClickable(role = Role.Button) { onEventDispatcher(SettingsEvent.EditNameClicked) }) {
                    LabeledValue(stringResource(R.string.settings_player_name), uiState.playerName)
                    OutlinedText(
                        text = stringResource(R.string.settings_edit),
                        fontSize = 16.sp,
                        color = GoldLight,
                        outlineColor = null,
                        shadowOffset = 2.dp,
                    )
                }
                SettingRow {
                    LabeledValue(stringResource(R.string.settings_language), stringResource(R.string.settings_language_value))
                    Text(stringResource(R.string.version_label, BuildConfig.VERSION_NAME), style = LabelStyle.copy(fontSize = 12.sp, letterSpacing = 0.sp))
                }
            }
        }

        WoodButton(
            text = stringResource(if (uiState.isSignedIn) R.string.settings_sign_out else R.string.settings_sign_in),
            onClick = { onEventDispatcher(SettingsEvent.AccountClicked) },
            modifier = Modifier.place(90.dp, 668.dp, 210.dp, 94.dp),
            fontSize = 28.sp,
        )
    }
}

@Composable
private fun SettingRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .background(Bark.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    SettingRow {
        OutlinedText(label, fontSize = 22.sp, shadowColor = null)
        WoodToggle(checked, onCheckedChange, contentDescription = label)
    }
}

@Composable
private fun LabeledValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = LabelStyle)
        OutlinedText(value, fontSize = 22.sp, shadowColor = null, textAlign = TextAlign.Start, maxLines = 1)
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun SettingsScreenPreview() {
    Puzzle15Theme {
        SettingsContent(uiState = SettingsContract.SettingsUiState(playerName = "Adilxan"), onEventDispatcher = {})
    }
}
