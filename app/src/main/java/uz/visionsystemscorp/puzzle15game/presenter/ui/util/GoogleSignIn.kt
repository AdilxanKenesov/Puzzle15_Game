package uz.visionsystemscorp.puzzle15game.presenter.ui.util

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import uz.visionsystemscorp.puzzle15game.R

/**
 * Tizimning Google akkaunt tanlash oynasini ochadi. Activity context kerak, shuning uchun UI qatlamida.
 * @return Firebase Auth uchun ID token yoki o'yinchi bekor qilsa null.
 */
suspend fun requestGoogleIdToken(activityContext: Context): String? {
    val option = GetSignInWithGoogleOption.Builder(activityContext.getString(R.string.default_web_client_id)).build()
    val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
    val credential = try {
        CredentialManager.create(activityContext).getCredential(activityContext, request).credential
    } catch (_: GetCredentialCancellationException) {
        return null
    } catch (e: NoCredentialException) {
        throw IllegalStateException("No Google account on this device", e)
    }
    check(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        "Unexpected credential type ${credential.type}"
    }
    return GoogleIdTokenCredential.createFrom(credential.data).idToken
}
