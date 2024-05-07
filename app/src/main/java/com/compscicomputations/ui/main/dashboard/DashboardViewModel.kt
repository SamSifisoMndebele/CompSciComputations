package com.compscicomputations.ui.main.dashboard

import androidx.lifecycle.ViewModel
import com.compscicomputations.ui.auth.FieldError
import com.compscicomputations.ui.auth.FieldType
import com.compscicomputations.ui.auth.added
import com.compscicomputations.ui.auth.contain
import com.compscicomputations.ui.auth.removed
import com.compscicomputations.ui.theme.emailRegex
import com.compscicomputations.ui.theme.namesRegex
import com.compscicomputations.utils.notMatches
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.OnEmailChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.EMAIL) {
                        it.copy(email = event.email, errors = it.errors removed FieldType.EMAIL)
                    } else {
                        it.copy(email = event.email)
                    }
                }
            }
            is DashboardUiEvent.OnImageUriChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.IMAGE) {
                        it.copy(imageUri = event.imageUri, errors = it.errors removed FieldType.IMAGE)
                    } else {
                        it.copy(imageUri = event.imageUri)
                    }
                }
            }
            is DashboardUiEvent.OnLastnameChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.LASTNAME) {
                        it.copy(lastname = event.lastname, errors = it.errors removed FieldType.LASTNAME)
                    } else {
                        it.copy(lastname = event.lastname)
                    }
                }
            }
            is DashboardUiEvent.OnNamesChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.NAMES) {
                        it.copy(names = event.names, errors = it.errors removed FieldType.NAMES)
                    } else {
                        it.copy(names = event.names)
                    }
                }
            }
            is DashboardUiEvent.OnUserTypeChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.USER_TYPE) {
                        it.copy(userType = event.userType, errors = it.errors removed FieldType.USER_TYPE)
                    } else {
                        it.copy(userType = event.userType)
                    }
                }
            }
        }
    }


    private fun checkForErrors(): Boolean {
        if (_uiState.value.names.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.NAMES, "Enter your name(s)."))
            }
        } else if (_uiState.value.names.notMatches(namesRegex)) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.NAMES, "Enter valid name(s)."))
            }
        }

        if (_uiState.value.lastname.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.LASTNAME, "Enter your lastname."))
            }
        } else if (_uiState.value.lastname.notMatches(namesRegex)) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.LASTNAME, "Enter a valid lastname."))
            }
        }

        if (_uiState.value.email.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.EMAIL, "Enter your email."))
            }
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.EMAIL, "Enter a valid email."))
            }
        }

        return _uiState.value.errors.isEmpty()
    }

    private fun register() {
        println("userType : ${_uiState.value.userType.name}")
        println("imageUri : ${_uiState.value.imageUri}")
        println("names    : ${_uiState.value.names}")
        println("lastname : ${_uiState.value.lastname}")
        println("email    : ${_uiState.value.email}")
    }
}