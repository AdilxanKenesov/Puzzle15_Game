package uz.visionsystemscorp.puzzle15game.presenter.screens.settings

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlin.coroutines.cancellation.CancellationException
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.screens.settings.SettingsContract.SettingsEvent
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ArtboardOverlay
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.SecondaryWoodTint
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ShadowedBodyText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopRightCloseButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopSign
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodToggle
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.pressClickable
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Cream
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.GoldLight
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.requestGoogleIdToken

@Composable
fun SettingsScreen() {
    val viewModel: SettingsContract.SettingsViewModel = hiltViewModel<SettingsViewModel>()
    val uiState = viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SettingsContract.SideEffect.LaunchGoogleReauth -> {
                val event = try {
                    requestGoogleIdToken(context)?.let(SettingsEvent::ReauthTokenReceived) ?: SettingsEvent.ReauthCancelled
                } catch (e: CancellationException) {
                    // The screen stopped while the picker was open: let the dialog be dismissed again.
                    viewModel.onEventDispatcher(SettingsEvent.ReauthCancelled)
                    throw e
                } catch (e: Exception) {
                    Log.w("GoogleSignIn", "Re-authentication failed", e)
                    Toast.makeText(context, R.string.settings_delete_error, Toast.LENGTH_LONG).show()
                    SettingsEvent.ReauthCancelled
                }
                viewModel.onEventDispatcher(event)
            }

            SettingsContract.SideEffect.ShowDeleteError ->
                Toast.makeText(context, R.string.settings_delete_error, Toast.LENGTH_LONG).show()
        }
    }

    SettingsContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
internal fun SettingsContent(
    uiState: SettingsContract.SettingsUiState,
    onEventDispatcher: (SettingsEvent) -> Unit,
) {
    ScreenBackground(
        scrimAlpha = 0.42f,
        overlay = {
            if (uiState.showDeleteConfirm) {
                DeleteAccountDialog(
                    isDeleting = uiState.isDeleting,
                    onConfirm = { onEventDispatcher(SettingsEvent.DeleteConfirmed) },
                    onDismiss = { onEventDispatcher(SettingsEvent.DeleteDismissed) },
                )
            }
        },
    ) {
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
                }
            }
        }

        WoodButton(
            text = stringResource(if (uiState.isSignedIn) R.string.settings_sign_out else R.string.settings_sign_in),
            onClick = { onEventDispatcher(SettingsEvent.AccountClicked) },
            modifier = Modifier.place(90.dp, 662.dp, 210.dp, 94.dp),
            fontSize = 28.sp,
        )

        if (uiState.hasAccount) {
            Text(
                text = stringResource(R.string.settings_delete_account),
                modifier = Modifier
                    .place(95.dp, 770.dp, 200.dp, 36.dp)
                    .pressClickable(role = Role.Button) { onEventDispatcher(SettingsEvent.DeleteAccountClicked) }
                    .wrapContentHeight(),
                style = LabelStyle.copy(fontSize = 14.sp, color = Cream, textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DeleteAccountDialog(isDeleting: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    ArtboardOverlay {
        WoodPanel(
            modifier = Modifier.place(25.dp, 250.dp, 340.dp, 290.dp).popIn(initialScale = 0.5f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                OutlinedText(stringResource(R.string.settings_delete_title), fontSize = 26.sp, color = GoldLight, shadowColor = null)
                ShadowedBodyText(stringResource(R.string.settings_delete_body))
            }
        }
        Row(
            modifier = Modifier.place(20.dp, 560.dp, 350.dp, 90.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            WoodButton(
                text = stringResource(R.string.action_cancel),
                onClick = onDismiss,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                fontSize = 22.sp,
                enabled = !isDeleting,
                tint = SecondaryWoodTint,
            )
            WoodButton(
                text = stringResource(R.string.settings_delete_confirm),
                onClick = onConfirm,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                fontSize = 22.sp,
                enabled = !isDeleting,
            )
        }
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
