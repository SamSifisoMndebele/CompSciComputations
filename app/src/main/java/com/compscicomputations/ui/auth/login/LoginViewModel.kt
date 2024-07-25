package com.compscicomputations.ui.auth.login

import android.util.Log
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
    private val request: GetCredentialRequest,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    companion object {
        private const val TAG = "LoginViewModel"
    }


    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
        if (!_uiState.value.canShowPassword && password.isBlank())
            _uiState.value = _uiState.value.copy(canShowPassword = true)
    }

    fun setCanShowPassword(showPassword: Boolean) {
        _uiState.value = _uiState.value.copy(canShowPassword = showPassword)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    private var loginJob: Job? = null

    fun onLogin(savePassword: suspend (email: String, password: String) -> Unit) {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Login..."))
        loginJob = viewModelScope.launch {
            try {
                authRepository.login(_uiState.value.email, _uiState.value.password)
                savePassword(_uiState.value.email, _uiState.value.password)
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: CreateCredentialException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
                Log.w(TAG, "onLogin::CreateCredentialException", e)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLogin::CancellationException", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "onLogin::Exception", e)
            }
        }
    }

    fun onContinueWithGoogle(
        getCredential: suspend (GetCredentialRequest) -> GetCredentialResponse
    ) {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Login with Google..."))

        loginJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val credential = getCredential(request).credential
                when {
                    credential is PasswordCredential -> {
                        Log.d(TAG, "PasswordCredential: ${credential.data}")
                        try {
                            authRepository.login(credential.id, credential.password)
                            _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
                        } catch (e: UnauthorizedException) {
                            _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                            Log.e(TAG, "PasswordCredential::UnauthorizedException", e)
                        } catch (e: ExpectationFailedException) {
                            _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                            Log.e(TAG, "PasswordCredential::ExpectationFailedException", e)
                        }
                    }
                    credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                        // Use googleIdTokenCredential and extract id to validate and authenticate on the server.
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        Log.d(TAG, "GoogleIdTokenCredential: ${googleIdTokenCredential.data}")

                        try {
                            authRepository.continueWithGoogle(googleIdTokenCredential.idToken)
                            _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)

                        } catch (e: UnauthorizedException) {
                            Log.e(TAG, "GoogleIdTokenCredential::UnauthorizedException", e)
                            _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                        } catch (e: ExpectationFailedException) {
                            Log.e(TAG, "GoogleIdTokenCredential::ExpectationFailedException", e)
                            _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                        }
                    }
                    else -> {
                        // Catch any unrecognized custom credential type here.
                        Log.e(TAG, "Unexpected type of credential")
                        throw Exception("Unexpected type of credential")
                    }
                }
            } catch (e: Exception) {
                handleFailure(e)
            }
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
    }
    private fun handleFailure(e: Exception) {
        when (e) {
            is CancellationException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, e)
            }
            is GetCredentialCancellationException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, e)
            }
            is GetCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, e)
            }
            is GetCredentialException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, e)
            }
            else -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "UnexpectedException:", e)
            }
        }
    }
}