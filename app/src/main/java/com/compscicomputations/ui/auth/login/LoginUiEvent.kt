package com.compscicomputations.ui.auth.login

sealed interface LoginUiEvent {
    data class OnEmailChange(val email: String): LoginUiEvent
    data class OnPasswordChange(val password: String): LoginUiEvent
    data class Login(val onLogin: () -> Unit, val onException: (Exception) -> Unit): LoginUiEvent
}