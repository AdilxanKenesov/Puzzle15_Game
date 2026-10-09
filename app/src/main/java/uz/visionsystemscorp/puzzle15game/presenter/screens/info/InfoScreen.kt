package uz.visionsystemscorp.puzzle15game.presenter.screens.info

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import uz.visionsystemscorp.puzzle15game.BuildConfig
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.InsetBox
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ShadowedBodyText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopRightCloseButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.TopSign
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.pressClickable
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BarkDeep
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.BodyStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Butter
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Gold
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.GoldLight
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.LabelStyle
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Peach
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Rust
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Sand

@Composable
fun InfoScreen() {
    val viewModel: InfoContract.InfoViewModel = hiltViewModel<InfoViewModel>()
    InfoContent(onEventDispatcher = viewModel::onEventDispatcher)
}

@Composable
private fun InfoContent(onEventDispatcher: (InfoContract.InfoEvent) -> Unit) {
    val onClose = { onEventDispatcher(InfoContract.InfoEvent.CloseClicked) }
    val uriHandler = LocalUriHandler.current
    // No browser / mail app installed must not crash the screen.
    val openUri = { uri: String -> runCatching { uriHandler.openUri(uri) } }
    val telegramUrl = stringResource(R.string.info_telegram_url)
    val emailUrl = stringResource(R.string.info_email_url)
    val privacyUrl = stringResource(R.string.info_privacy_url)

    ScreenBackground(scrimAlpha = 0.45f) {
        TopSign(stringResource(R.string.info_title), fontSize = 30.sp)
        TopRightCloseButton(onClose)

        WoodPanel(Modifier.place(15.dp, 190.dp, 360.dp, 470.dp).popIn(delayMillis = 150, initialScale = 0.3f)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionHeading(stringResource(R.string.info_game_heading))
                    ShadowedBodyText(stringResource(R.string.info_game_body), textAlign = TextAlign.Start)
                }
                Divider()
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeading(stringResource(R.string.info_developer_heading))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Avatar(stringResource(R.string.info_developer_initials))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            OutlinedText(
                                text = stringResource(R.string.info_developer_name),
                                fontSize = 22.sp,
                                shadowColor = null,
                                textAlign = TextAlign.Start,
                            )
                            Text(stringResource(R.string.info_developer_bio), style = BodyStyle.copy(fontSize = 13.sp, color = Sand))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LinkButton(stringResource(R.string.info_telegram), Modifier.weight(1f)) { openUri(telegramUrl) }
                        LinkButton(stringResource(R.string.info_email), Modifier.weight(1f)) { openUri(emailUrl) }
                    }
                }
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(stringResource(R.string.info_version, BuildConfig.VERSION_NAME) + " · ", style = LabelStyle)
                    Text(
                        text = stringResource(R.string.info_privacy),
                        modifier = Modifier.pressClickable(role = Role.Button) { openUri(privacyUrl) },
                        style = LabelStyle.copy(textDecoration = TextDecoration.Underline),
                    )
                }
            }
        }

        WoodButton(
            text = stringResource(R.string.action_back),
            onClick = onClose,
            modifier = Modifier.place(90.dp, 690.dp, 210.dp, 94.dp),
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    OutlinedText(text, fontSize = 22.sp, color = GoldLight, shadowColor = null, textAlign = TextAlign.Start)
}

@Composable
private fun Divider() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(3.dp)
            .drawBehind {
                val radius = CornerRadius(2.dp.toPx())
                drawRoundRect(Peach, Offset(0f, 2.dp.toPx()), size, radius)
                drawRoundRect(Bark, cornerRadius = radius)
            },
    )
}

@Composable
private fun Avatar(initials: String) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .drawBehind { drawCircle(BarkDeep, center = center + Offset(0f, 3.dp.toPx())) }
            .background(Gold, CircleShape)
            .border(4.dp, Bark, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        OutlinedText(initials, fontSize = 24.sp, color = Rust, outlineColor = null, shadowColor = Butter, shadowOffset = 2.dp)
    }
}

@Composable
private fun LinkButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    InsetBox(modifier.height(44.dp).pressClickable(onClick = onClick), cornerRadius = 12.dp) {
        OutlinedText(text, fontSize = 15.sp, color = GoldLight, outlineColor = null, shadowColor = null)
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun InfoScreenPreview() {
    Puzzle15Theme { InfoContent(onEventDispatcher = {}) }
}
