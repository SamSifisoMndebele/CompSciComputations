package com.compscicomputations.ui.auth.login

import com.compscicomputations.ui.auth.FieldError


data class LoginUiState(
    val email: String = "",
    val password: String = "",

    val errors: Set<FieldError> = setOf(),
)