package com.compscicomputations.ui.auth.login

import android.app.Activity
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.auth.data.source.AuthRepository.Companion
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.ExpectationFailedException
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.UnauthorizedException
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
    fun onLoginPassword(
        navigatePasswordLogin: () -> Unit,
        getCredential: suspend (GetCredentialRequest) -> GetCredentialResponse
    ) {
        _uiState.value = ProgressState.Loading("Continue with Password...")

        loginJob = viewModelScope.launch {
            try {
                val credential = getCredential(passwordRequest).credential
                if (credential is PasswordCredential) {
                    // Use id and password to send to the server to validate and authenticate

                    _uiState.value = ProgressState.Loading("Login...")
                    try {
                        authRepository.continuePassword(credential.id, credential.password)
                        _uiState.value = ProgressState.Success
                    } catch (e: Exception) {
                        authRepository.savePasswordCredentials(credential.id, credential.password)
                        navigatePasswordLogin()
                        _uiState.value = ProgressState.Idle
                        Log.e("LoginViewModel", "onLoginPassword", e)
                    }

                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                    throw Exception("Unexpected type of credential")
                }
            } catch (e: NoCredentialException) {
                authRepository.clearUserCredentials()
                navigatePasswordLogin()
                _uiState.value = ProgressState.Idle
                Log.w(TAG, e)
            } catch (e: GetCredentialCancellationException) {
                authRepository.clearUserCredentials()
                navigatePasswordLogin()
                _uiState.value = ProgressState.Idle
                Log.w(TAG, e)
            } catch (e: Exception) {
                Log.e(TAG, "saveCredentials::error", e)
                authRepository.clearUserCredentials()
                _uiState.value = ProgressState.Error(e.localizedMessage)
                Log.w(TAG, e)
            }
        }
    }

    fun onLoginWithGoogle(
        getCredential: suspend (GetCredentialRequest) -> GetCredentialResponse
    ) {
        _uiState.value = ProgressState.Loading("Login with Google...")

        loginJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val credential = getCredential(googleRequest).credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)  {
                    // Use googleIdTokenCredential and extract id to validate and authenticate on the server.
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    Log.d(TAG, "GoogleIdTokenCredential: ${googleIdTokenCredential.data}")

                    try {
                        authRepository.continueWithGoogle(googleIdTokenCredential)
                        _uiState.value = ProgressState.Success

                    } catch (e: UnauthorizedException) {
                        Log.e(TAG, "UnauthorizedException::continueWithGoogle", e)
                        _uiState.value = ProgressState.Error(e.localizedMessage)
                    } catch (e: ExpectationFailedException) {
                        Log.e(TAG, "ExpectationFailedException::continueWithGoogle", e)
                        _uiState.value = ProgressState.Error(e.localizedMessage)
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception::continueWithGoogle", e)
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