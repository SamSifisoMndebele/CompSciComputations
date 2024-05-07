package com.compscicomputations.ui.auth.login

import androidx.lifecycle.ViewModel
import com.compscicomputations.ui.auth.FieldError
import com.compscicomputations.ui.auth.FieldType
import com.compscicomputations.ui.auth.added
import com.compscicomputations.ui.auth.contain
import com.compscicomputations.ui.auth.removed
import com.compscicomputations.ui.theme.emailRegex
import com.compscicomputations.utils.notMatches
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.EMAIL) {
                        it.copy(email = event.email, errors = it.errors removed FieldType.EMAIL)
                    } else {
                        it.copy(email = event.email)
                    }
                }
            }
            is LoginUiEvent.OnPasswordChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.PASSWORD) {
                        it.copy(password = event.password, errors = it.errors.removed(FieldType.PASSWORD))
                    } else {
                        it.copy(password = event.password)
                    }
                }
            }
            is LoginUiEvent.Login -> {
                if (checkForErrors()) {
                    println("email   : ${_uiState.value.email}")
                    println("password: ${_uiState.value.password}")
                    if (_uiState.value.email == "sams@mail.com" && _uiState.value.password == "123456")
                        event.onLogin()
                    else
                        event.onException(Exception("Incorrect password."))
                }

            }
        }
    }

    private fun checkForErrors(): Boolean {
        if (_uiState.value.email.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.EMAIL, "Enter your email."))
            }
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.EMAIL, "Enter a valid email."))
            }
        }

        if (_uiState.value.password.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.PASSWORD, "Password can not be blank."))
            }
        }
        return _uiState.value.errors.isEmpty()
    }
}