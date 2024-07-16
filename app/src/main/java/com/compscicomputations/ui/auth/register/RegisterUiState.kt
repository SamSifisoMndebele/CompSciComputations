package com.compscicomputations.ui.auth.register

import android.net.Uri
import com.compscicomputations.ui.utils.ProgressState

data class RegisterUiState(
    val photoUri: Uri? = null,
    val photoUrl: String? = null,
    val names: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    val namesError: String? = null,
    val lastnameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
}