package com.compscicomputations.ui.auth.register

import android.net.Uri
import com.compscicomputations.ui.auth.FieldError
import com.compscicomputations.ui.auth.UserType

data class RegisterUiState(
    val userType: UserType = UserType.STUDENT,
    val adminCode: String? = null,
    val names: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val imageUri: Uri? = null,
    val termsAccepted: Boolean = false,

    val errors: Set<FieldError> = setOf()
)
