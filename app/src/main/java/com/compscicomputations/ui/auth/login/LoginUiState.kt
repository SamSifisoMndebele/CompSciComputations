package com.compscicomputations.ui.auth.login

import com.compscicomputations.ui.utils.ProgressState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val progressState: ProgressState = ProgressState.Idle
)