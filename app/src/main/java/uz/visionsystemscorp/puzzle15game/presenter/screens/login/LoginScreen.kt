package uz.visionsystemscorp.puzzle15game.presenter.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlin.coroutines.cancellation.CancellationException
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.visionsystemscorp.puzzle15game.navigation.LoginRoute
import uz.visionsystemscorp.puzzle15game.R
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContract.LoginEvent
import uz.visionsystemscorp.puzzle15game.presenter.screens.login.LoginContract.SideEffect
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.dropIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.popIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.animation.slideIn
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.BoardPanel
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.OutlinedText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ScreenBackground
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.ShadowedBodyText
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.SignHeader
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.WoodButton
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.place
import uz.visionsystemscorp.puzzle15game.presenter.ui.components.woodTint
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Bark
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.GoldLight
import uz.visionsystemscorp.puzzle15game.presenter.ui.theme.Puzzle15Theme
import uz.visionsystemscorp.puzzle15game.presenter.ui.util.requestGoogleIdToken

private val GuestTint = woodTint(saturation = 0.75f, brightness = 0.92f)

@Composable
fun LoginScreen(route: LoginRoute) {
    val viewModel: LoginContract.LoginViewModel =
        hiltViewModel<LoginViewModel, LoginViewModel.Factory>(creationCallback = { it.create(route) })
    val uiState = viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SideEffect.LaunchGoogleSignIn -> {
                val event = try {
                    requestGoogleIdToken(context)?.let(LoginEvent::GoogleTokenReceived) ?: LoginEvent.GoogleCancelled
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.w("GoogleSignIn", "Account picker failed", e)
                    LoginEvent.GoogleFailed
                }
                viewModel.onEventDispatcher(event)
            }

            SideEffect.ShowSignInError ->
                Toast.makeText(context, R.string.login_error, Toast.LENGTH_LONG).show()
        }
    }

    LoginContent(uiState = uiState.value, onEventDispatcher = viewModel::onEventDispatcher)
}

/** Sign-in choice: Google keeps records across devices, a guest account only on this one. */
@Composable
private fun LoginContent(
    uiState: LoginContract.LoginUiState,
    onEventDispatcher: (LoginEvent) -> Unit,
) {
    ScreenBackground(scrimAlpha = 0.4f) {
        SignHeader(
            title = stringResource(R.string.login_title),
            modifier = Modifier.place(45.dp, 70.dp, 300.dp, 222.dp).dropIn(),
            fontSize = 34.sp,
        )

        BoardPanel(
            modifier = Modifier.place(10.dp, 300.dp, 370.dp, 277.dp).popIn(delayMillis = 350),
            horizontalPadding = 82.dp,
            topPadding = 40.dp,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                OutlinedText(stringResource(R.string.login_heading), fontSize = 24.sp, color = GoldLight, shadowColor = null)
                ShadowedBodyText(stringResource(R.string.login_body))
            }
        }

        WoodButton(
            text = stringResource(R.string.login_google),
            onClick = { onEventDispatcher(LoginEvent.GoogleClicked) },
            enabled = !uiState.isLoading,
            modifier = Modifier.place(65.dp, 604.dp, 260.dp, 117.dp).slideIn(delayMillis = 700),
            fontSize = 28.sp,
            leadingIcon = { GoogleBadge() },
        )
        WoodButton(
            text = stringResource(R.string.login_guest),
            onClick = { onEventDispatcher(LoginEvent.GuestClicked) },
            enabled = !uiState.isLoading,
            modifier = Modifier.place(95.dp, 728.dp, 200.dp, 90.dp).slideIn(delayMillis = 850),
            fontSize = 22.sp,
            tint = GuestTint,
        )
    }
}

@Composable
private fun GoogleBadge() {
    Box(
        modifier = Modifier
            .size(38.dp)
            .background(Color.White, CircleShape)
            .border(3.dp, Bark, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Image(painterResource(R.drawable.ic_google), contentDescription = null, modifier = Modifier.size(22.dp))
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun LoginScreenPreview() {
    Puzzle15Theme { LoginContent(uiState = LoginContract.LoginUiState(), onEventDispatcher = {}) }
}
