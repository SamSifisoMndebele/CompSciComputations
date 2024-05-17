package com.compscicomputations.ui.auth.login

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val credentialRequest: GetCredentialRequest
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    var emailError by mutableStateOf<String?>(null)

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    var passwordError by mutableStateOf<String?>(null)

    val snackBarHostState = SnackbarHostState()
    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _userLogged = MutableStateFlow(false)
    val userLogged = _userLogged.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onLogin() {
        _showProgress.value = true
        auth.signInWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "onSignIn:success")
                    _name.value = task.result?.user?.displayName
                    _userLogged.value = true
                } else {
                    Log.w("LoginViewModel", "onSignIn:failure", task.exception)
                    viewModelScope.launch {
                        task.exception?.message?.let {
                            snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                        }
                    }
                    _showProgress.value = false
                }
            }
    }

    fun continueWithGoogle(context: Context) {
        _showProgress.value = true
        viewModelScope.launch {
            val result = try {
                credentialManager.getCredential(
                    request = credentialRequest,
                    context = context,
                )
            } catch (e: NoCredentialException) {
                Log.e("LoginViewModel", e.message, e)
                _showProgress.value = false
                return@launch
            } catch (e: Exception) {
                Log.e("LoginViewModel", e.message, e)
                _showProgress.value = false
                return@launch
            }

            val googleIdToken = when (val credential = result.credential) {
                is CustomCredential -> if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        GoogleIdTokenCredential.createFrom(credential.data).idToken
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("LoginViewModel", "Received an invalid google id token response", e)
                        _showProgress.value = false
                        null
                    }
                } else {
                    Log.e("LoginViewModel", "Unexpected type of credential")
                    _showProgress.value = false
                    null
                }
                else -> {
                    Log.e("LoginViewModel", "Unexpected type of credential")
                    _showProgress.value = false
                    null
                }
            }
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginViewModel", "continueWithGoogle:success")
                        _name.value = task.result?.user?.displayName
                        _userLogged.value = true
                    } else {
                        Log.w("LoginViewModel", "continueWithGoogle:failure", task.exception)
                        viewModelScope.launch {
                            task.exception?.message?.let {
                                snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                            }
                        }
                        _showProgress.value = false
                    }
                }
        }
    }

    fun onSendSignInLink() {
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://www.example.com/finishSignUp?cartId=1234"
            handleCodeInApp = true // This must be true
            setAndroidPackageName(
                "com.compscicomputations",
                true, // installIfNotAvailable
                "1", // minimumVersion
            )
        }
        Firebase.auth.sendSignInLinkToEmail(_email.value, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Email sent.")
                }
            }
    }

    fun onLinkLogin(emailLink: String): Boolean {
        val isLinkSignIn = auth.isSignInWithEmailLink(emailLink)
        if (isLinkSignIn)
            auth.signInWithEmailLink(_email.value, emailLink)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "Successfully signed in with email link!")
                        val result = task.result
                        // You can access the new user via result.getUser()
                        // Additional user info profile *not* available via:
                        // result.getAdditionalUserInfo().getProfile() == null
                        // You can check if the user is new or existing:
                        // result.getAdditionalUserInfo().isNewUser()
                    } else {
                        Log.e("TAG", "Error signing in with email link", task.exception)
                    }
                }
        return isLinkSignIn
    }
}