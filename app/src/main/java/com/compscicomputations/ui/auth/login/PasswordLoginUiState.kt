package com.compscicomputations.ui.auth.login

import com.compscicomputations.ui.utils.ProgressState

data class PasswordLoginUiState(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,

    val progressState: ProgressState = ProgressState.Idle,
    val canShowPassword: Boolean = false
)