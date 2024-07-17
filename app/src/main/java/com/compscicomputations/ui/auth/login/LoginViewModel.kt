package com.compscicomputations.ui.auth.login

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.client.auth.AuthRepository
import com.compscicomputations.core.client.auth.usecase.IsCompleteProfileUseCase
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val request: GetCredentialRequest,
    private val authRepository: AuthRepository,
    private val isCompleteProfileUseCase: IsCompleteProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    private var loginJob: Job? = null
    fun onLogin() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Login..."))
        loginJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.login(uiState.value.email, uiState.value.password)
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w("LoginViewModel", "onLoginWithGoogle:", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e("LoginViewModel", "onLoginWithGoogle:", e)
            }
        }
    }

    fun onLoginWithGoogle(activity: Activity) {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Login with Google..."))
        val credentialManager = CredentialManager.create(activity)

        loginJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.getCredential(activity, request)
                handleSignIn(result)
            } catch (e: Exception) {
                handleFailure(e)
            }
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }

    private suspend fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        Log.d(TAG, "credential: ${result.credential.data}")
        when (val credential = result.credential) {
            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                // Use id and password to send to the server to validate and authenticate

                Log.d(TAG, "username: $username")
                Log.d(TAG, "password: $password")

            }
            else -> {
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)  {
                    // Use googleIdTokenCredential and extract id to validate and authenticate on the server.
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    val isNewUser = authRepository.continueWithGoogle(googleIdTokenCredential) ||
                            !isCompleteProfileUseCase().single()

                    _uiState.value = _uiState.value.copy(
                        isNewUser = isNewUser,
                        progressState = ProgressState.Success
                    )
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                    throw Exception("Unexpected type of credential")
                }
            }
        }
    }

    private fun handleFailure(e: Exception) {
        when (e) {
            is CancellationException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            is GetCredentialCancellationException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            is GetCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            is GetCredentialException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            else -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "Unexpected exception::onLoginWithGoogle:", e)
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}