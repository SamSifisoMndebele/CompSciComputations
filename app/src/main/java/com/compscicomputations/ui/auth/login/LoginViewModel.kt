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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    val passwordError by mutableStateOf<String?>(null)

    val snackBarHostState = SnackbarHostState()

    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _userSignedIn = MutableStateFlow(false)
    val userSignedIn = _userSignedIn.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignIn() {
        _showProgress.value = true
        auth.signInWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "onSignIn:success")
                    _name.value = task.result?.user?.displayName
                    _userSignedIn.value = true
                } else {
                    Log.w("LoginViewModel", "onSignIn:failure", task.exception)
                    viewModelScope.launch {
                        task.exception?.message?.let {
                            snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                        }
                    }
                }
                _showProgress.value = false
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
                        _userSignedIn.value = true
                    } else {
                        Log.w("LoginViewModel", "continueWithGoogle:failure", task.exception)
                        viewModelScope.launch {
                            task.exception?.message?.let {
                                snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                            }
                        }
                    }
                    _showProgress.value = false
                }
        }
    }
}