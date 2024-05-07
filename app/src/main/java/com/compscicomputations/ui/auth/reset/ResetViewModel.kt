package com.compscicomputations.ui.auth.reset

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

class ResetViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ResetUiState())
    val uiState: StateFlow<ResetUiState> = _uiState.asStateFlow()

    fun onEvent(event: ResetUiEvent) {
        when (event) {
            is ResetUiEvent.OnEmailChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.EMAIL) {
                        it.copy(email = event.email, errors = it.errors removed FieldType.EMAIL)
                    } else {
                        it.copy(email = event.email)
                    }
                }
            }
            ResetUiEvent.Reset -> {
                if (checkForErrors()) reset()
            }
        }
    }

    private fun checkForErrors(): Boolean {
        if (_uiState.value.email.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.EMAIL, "Enter your email."))
            }
        } else if (_uiState.value.email notMatches emailRegex) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.EMAIL, "Enter a valid email."))
            }
        }
        return _uiState.value.errors.isEmpty()
    }

    private fun reset() {
        println("email   : ${_uiState.value.email}")


    }
}