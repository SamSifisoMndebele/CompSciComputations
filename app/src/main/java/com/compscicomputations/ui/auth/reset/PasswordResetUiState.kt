package com.compscicomputations.ui.auth.reset

import com.compscicomputations.ui.utils.ProgressState

data class PasswordResetUiState(
    val email: String = "",
    val otp: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    val emailError: String? = null,
    val otpError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,

    val sendingOtp: Boolean = false,
    val progressState: ProgressState = ProgressState.Idle
) {
    val isValid get() = email.isNotBlank() && otp.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
}
