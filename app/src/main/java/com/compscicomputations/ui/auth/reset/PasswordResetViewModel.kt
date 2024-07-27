package com.compscicomputations.ui.auth.reset

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.auth.data.source.remote.NewPassword
import com.compscicomputations.theme.emailRegex
import com.compscicomputations.theme.strongPasswordRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
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

    fun onSendOtp() {
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter your email.")
            return
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid email.")
            return
        }

        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Sending OTP..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            authRepository.requestOtp(_uiState.value.email)
            _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle, otpSent = true)
        }

    }

    fun onPasswordReset() {
        if (!fieldsAreValid()) return

        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Resetting password..."))
        job = viewModelScope.launch {
            authRepository.passwordReset(
                email = _uiState.value.email,
                otp = _uiState.value.otp,
                password = _uiState.value.password,
            )
            _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
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
}