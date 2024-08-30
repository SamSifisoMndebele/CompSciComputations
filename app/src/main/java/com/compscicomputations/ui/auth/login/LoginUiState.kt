package com.compscicomputations.ui.auth.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,

    val canShowPassword: Boolean = false
) {
    val isValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}