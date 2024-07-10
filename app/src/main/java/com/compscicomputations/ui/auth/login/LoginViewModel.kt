package com.compscicomputations.ui.auth.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
//    private val loginUseCase: LoginUseCase,
    private val authRepository: AuthRepository
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
                Log.e("LoginViewModel", "onLoginWithGoogle:", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e("LoginViewModel", "onLoginWithGoogle:", e)
            }
        }
    }

    fun onLoginWithGoogle(activity: Activity) {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Login with Google..."))
        loginJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.loginWithGoogle(activity)
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.e("LoginViewModel", "onLoginWithGoogle:", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e("LoginViewModel", "onLoginWithGoogle:", e)
            }
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }
}