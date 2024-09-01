package com.compscicomputations.ui.auth.reset

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.OtpException
import com.compscicomputations.di.IoDispatcher
import com.compscicomputations.theme.emailRegex
import com.compscicomputations.theme.strongPasswordRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordResetUiState())
    val uiState: StateFlow<PasswordResetUiState> = _uiState.asStateFlow()

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }
    fun onOTPChange(otp: String) {
        _uiState.value = _uiState.value.copy(otp = otp, otpError = null)
    }
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }
    fun onPasswordConfirmChange(passwordConfirm: String) {
        _uiState.value = _uiState.value.copy(passwordConfirm = passwordConfirm, passwordConfirmError = null)
    }

    private var job: Job? = null

    suspend fun onSendOtp(success: () -> Unit) {
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter your email.")
            return
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid email.")
            return
        }

        _uiState.value = _uiState.value.copy(sendingOtp = true)
        try {
            authRepository.passwordResetOtp(_uiState.value.email)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(sendingOtp = false, progressState  = ProgressState.Error(e.localizedMessage))
            return
        }
        _uiState.value = _uiState.value.copy(sendingOtp = false, otp = "")
        success()
    }

    fun onPasswordReset() {
        if (!fieldsAreValid()) return

        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Resetting password..."))
        job = viewModelScope.launch {
            try {
                authRepository.passwordResetOtp(
                    email = _uiState.value.email,
                    otp = _uiState.value.otp,
                    newPassword = _uiState.value.password,
                )
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: OtpException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle, otpError = e.localizedMessage)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onRegister", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "onRegister", e)
            }
        }
    }

    fun cancel() {
        job?.cancel()
    }

    private fun fieldsAreValid(): Boolean {
        var valid = true
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter your email.")
            valid = false
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid email.")
            valid = false
        }
        if (_uiState.value.otp.isBlank()) {
            _uiState.value = _uiState.value.copy(otpError = "Enter the OTP sent to your email above.")
            valid = false
        } else if (!_uiState.value.otp.isDigitsOnly()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid OTP.")
            valid = false
        }
        if (_uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Create a password.")
            valid = false
        } else if (_uiState.value.password.notMatches(strongPasswordRegex)) {
            _uiState.value = _uiState.value.copy(passwordError = "Create a strong password.\n" +
                    "Password should be 6 characters length and contain at least:\n" +
                    "-a capital letter\n" +
                    "-a small letter\n" +
                    "-a number\n" +
                    "-a special character\n")
            valid = false
        }
        if (_uiState.value.passwordConfirm.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordConfirmError = "Confirm your password.")
            valid = false
        } else if (_uiState.value.passwordConfirm != _uiState.value.password) {
            _uiState.value = _uiState.value.copy(passwordConfirmError = "Passwords do not match.")
            valid = false
        }
        return valid
    }

    companion object {
        private const val TAG = "PasswordResetViewModel"
    }
}