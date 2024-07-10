package com.compscicomputations.ui.auth.register

import android.net.Uri
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.compscicomputations.ui.utils.ProgressState

data class RegisterUiState(
    val usertype: Usertype = Usertype.STUDENT,
    val adminCode: String? = null,
    val photoUri: Uri? = null,
    val photoUrl: String? = null,
    val displayName: String = "",
    val email: String = "",
    val phone: String? = null,
    val password: String = "",
    val passwordConfirm: String = "",
    val termsAccepted: Boolean = false,

    val adminCodeError: String? = null,
    val displayNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isAdmin: Boolean
        get() = usertype == Usertype.ADMIN

    val isValid: Boolean
        get() = displayName.isNotBlank() && email.isNotBlank() &&
                password.isNotBlank() && passwordConfirm.isNotBlank() &&
                termsAccepted && (!isAdmin || isAdmin && adminCode?.isNotBlank() == true)
}