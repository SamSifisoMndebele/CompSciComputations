package com.compscicomputations.ui.auth.login

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("google")
    private val googleRequest: GetCredentialRequest,
    @Named("password")
    private val passwordRequest: GetCredentialRequest,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProgressState>(ProgressState.Idle)
    val uiState: StateFlow<ProgressState> = _uiState.asStateFlow()

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = progressState
    }

    private var loginJob: Job? = null
    fun onLoginPassword(activity: Activity, navigatePasswordLogin: () -> Unit) {
        _uiState.value = ProgressState.Loading("Continue with Password...")
        val credentialManager = CredentialManager.create(activity)

        loginJob = viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(activity, passwordRequest)
                val credential = result.credential
                if (credential is PasswordCredential) {
                    // Use id and password to send to the server to validate and authenticate
                    val email = credential.id
                    val password = credential.password

                    Log.d(TAG, "PasswordCredential: ${credential.data}")
                    Log.d(TAG, "email: $email")
                    Log.d(TAG, "password: $password")
                    try {
                        authRepository.savePasswordCredentials(email, password)
                        navigatePasswordLogin()
                        _uiState.value = ProgressState.Idle
                    } catch (e: Exception) {
                        Log.e(TAG, "saveCredentials::error", e)
                        authRepository.clearUserCredentials()
                        _uiState.value = ProgressState.Error(e.localizedMessage)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                    throw Exception("Unexpected type of credential")
                }
            } catch (e: Exception) {
                authRepository.clearUserCredentials()
                when(e) {
                    is NoCredentialException -> navigatePasswordLogin()
                    is GetCredentialCancellationException -> navigatePasswordLogin()
                }
                Log.w(TAG, e)
                _uiState.value = ProgressState.Idle
            }
        }
    }

    fun onLoginWithGoogle(activity: Activity) {
        _uiState.value = ProgressState.Loading("Login with Google...")
        val credentialManager = CredentialManager.create(activity)

        loginJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(activity, googleRequest)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)  {
                    // Use googleIdTokenCredential and extract id to validate and authenticate on the server.
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    Log.d(TAG, "GoogleIdTokenCredential: ${googleIdTokenCredential.data}")
                    try {
                        authRepository.continueWithGoogle(googleIdTokenCredential)
                        _uiState.value = ProgressState.Success
                    } catch (e: Exception) {
                        Log.e(TAG, "continueWithGoogle::error", e)
                        _uiState.value = ProgressState.Error(e.localizedMessage)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                    throw Exception("Unexpected type of credential")
                }
            } catch (e: Exception) {
                handleFailure(e)
            }
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }

    private fun handleFailure(e: Exception) {
        when (e) {
            is CancellationException -> {
                _uiState.value = ProgressState.Idle
                Log.w(TAG, e)
            }
            is GetCredentialCancellationException -> {
                _uiState.value = ProgressState.Idle
                Log.w(TAG, e)
            }
            is GetCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
                _uiState.value = ProgressState.Idle
                Log.w(TAG, e)
            }
            is GetCredentialException -> {
                _uiState.value = ProgressState.Idle
                Log.w(TAG, e)
            }
            else -> {
                _uiState.value = ProgressState.Error(e.localizedMessage)
                Log.e(TAG, "UnexpectedException:", e)
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}