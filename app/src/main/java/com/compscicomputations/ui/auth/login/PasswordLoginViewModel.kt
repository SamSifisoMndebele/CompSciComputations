package com.compscicomputations.ui.auth.login

import android.app.Activity
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PasswordLoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordLoginUiState())
    val uiState: StateFlow<PasswordLoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.passwordCredentialsFlow
                .catch { e ->
                    Log.e(TAG, "passwordCredentialsFlow::error", e)
                }
                .first()
                .let { credentials ->
                    _uiState.value = _uiState.value.copy(
                        password = credentials?.password ?: "",
                        email = credentials?.email ?: "",
                        canShowCredentials = credentials == null
                    )
                    if (credentials != null) onLogin()
                }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
        if (!_uiState.value.canShowCredentials && password.isBlank())
            _uiState.value = _uiState.value.copy(canShowCredentials = true)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    private var loginJob: Job? = null
    fun onLogin(savePassword: suspend (email: String, password: String) -> Unit = {_,_->}) {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Login..."))
        loginJob = viewModelScope.launch {
            try {
                authRepository.continuePassword(_uiState.value.email, _uiState.value.password)
                savePassword(_uiState.value.email, _uiState.value.password)
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: CreateCredentialException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
                Log.w("LoginViewModel", "onLoginPassword::savePassword", e)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w("LoginViewModel", "onLoginPassword", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e("LoginViewModel", "onLoginPassword", e)
            }
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }

    companion object {
        private const val TAG = "PasswordLoginViewModel"
    }
}