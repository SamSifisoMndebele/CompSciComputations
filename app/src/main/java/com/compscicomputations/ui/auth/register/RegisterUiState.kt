package com.compscicomputations.ui.auth.register

import com.compscicomputations.ui.utils.ProgressState

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val termsAccepted: Boolean = false,

    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,
    val termsAcceptedError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isValid: Boolean
        get() = email.isNotBlank() && termsAccepted &&
                password.isNotBlank() && passwordConfirm.isNotBlank()
}