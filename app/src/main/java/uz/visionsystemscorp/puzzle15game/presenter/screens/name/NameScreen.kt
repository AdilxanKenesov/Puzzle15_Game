package uz.visionsystemscorp.puzzle15game.presenter.screens.name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.navigation.NameRoute
import uz.visionsystemscorp.puzzle15game.domain.model.PlayerProfile
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.dropIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.pulse
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.BoardPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.InsetBox
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ShadowedBodyText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.SignHeader
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Clay
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Cream
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LuckiestGuy
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme

@Composable
fun NameScreen(route: NameRoute) {
    val viewModel: NameContract.NameViewModel =
        hiltViewModel<NameViewModel, NameViewModel.Factory>(creationCallback = { it.create(route) })
    val uiState = viewModel.collectAsState()
    NameContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
internal fun NameContent(
    uiState: NameContract.NameUiState,
    onEventDispatcher: (NameContract.NameEvent) -> Unit,
) {
    val onSave = { onEventDispatcher(NameContract.NameEvent.SaveClicked) }
    ScreenBackground(scrimAlpha = 0.45f) {
        SignHeader(
            title = stringResource(R.string.name_title),
            modifier = Modifier.place(45.dp, 90.dp, 300.dp, 222.dp).dropIn(),
            fontSize = 32.sp,
        )

        BoardPanel(
            modifier = Modifier.place(10.dp, 330.dp, 370.dp, 277.dp).popIn(delayMillis = 350),
            horizontalPadding = 80.dp,
            topPadding = 46.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                ShadowedBodyText(stringResource(R.string.name_body))
                NameField(
                    value = uiState.name,
                    onValueChange = { onEventDispatcher(NameContract.NameEvent.NameChanged(it)) },
                    onDone = onSave,
                    modifier = Modifier.size(206.dp, 56.dp),
                )
                Text(
                    text = stringResource(R.string.name_hint, PlayerProfile.NAME_MIN_LENGTH, PlayerProfile.NAME_MAX_LENGTH),
                    style = LabelStyle,
                )
            }
        }

        WoodButton(
            text = stringResource(R.string.action_save),
            onClick = onSave,
            modifier = Modifier.place(90.dp, 650.dp, 210.dp, 94.dp).pulse(),
            enabled = uiState.canSave,
        )
    }
}

@Composable
private fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle = TextStyle(
        fontFamily = LuckiestGuy,
        fontSize = 22.sp,
        letterSpacing = 1.sp,
        color = Cream,
        textAlign = TextAlign.Center,
    )
    val label = stringResource(R.string.name_label)
    InsetBox(modifier, cornerRadius = 16.dp, borderWidth = 4.dp) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 6.dp)
                .semantics { contentDescription = label },
            textStyle = textStyle,
            singleLine = true,
            cursorBrush = SolidColor(Cream),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.Center) {
                    if (value.isEmpty()) {
                        Text(stringResource(R.string.name_placeholder), style = textStyle.copy(color = Clay))
                    }
                    innerTextField()
                }
            },
        )
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun NameScreenPreview() {
    Puzzle15Theme { NameContent(NameContract.NameUiState(name = "Adilxan"), onEventDispatcher = {}) }
}
