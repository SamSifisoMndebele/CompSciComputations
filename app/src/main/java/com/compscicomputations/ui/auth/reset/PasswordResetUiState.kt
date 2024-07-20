package com.compscicomputations.ui.auth.reset

import com.compscicomputations.ui.utils.ProgressState

data class PasswordResetUiState(
    val email: String = "",
    val password: String = "",
    val otp: String = "",
    val passwordConfirm: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,
    val otpError: String? = null,
    val passwordConfirmError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
)
