package com.compscicomputations.ui.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.ui.auth.FieldError
import com.compscicomputations.ui.auth.FieldType
import com.compscicomputations.ui.auth.UserType
import com.compscicomputations.ui.auth.added
import com.compscicomputations.ui.auth.contain
import com.compscicomputations.ui.auth.removed
import com.compscicomputations.ui.theme.emailRegex
import com.compscicomputations.ui.theme.namesRegex
import com.compscicomputations.ui.theme.strongPasswordRegex
import com.compscicomputations.utils.notMatches
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    var emailError by mutableStateOf<String?>(null)

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    val passwordError by mutableStateOf<String?>(null)

    var error by mutableStateOf<String?>(null)

    var showProgress by mutableStateOf(false)

    private val _termsAccepted = MutableStateFlow(false)
    val termsAccepted = _termsAccepted.asStateFlow()
    fun setTermsAccepted(accepted: Boolean) {
        _termsAccepted.value = accepted
    }

    fun onSignIn() {
        showProgress = true
        auth.createUserWithEmailAndPassword(_email.value, _password.value)
        auth.signInWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener {
                showProgress = false
            }
            .addOnFailureListener {
                error = it.message
                viewModelScope.launch {
                    delay(3000)
                    error = null
                }
            }
    }


    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.OnUserTypeChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.USER_TYPE) {
                        it.copy(userType = event.userType, errors = it.errors removed FieldType.USER_TYPE)
                    } else {
                        it.copy(userType = event.userType)
                    }
                }
            }
            is RegisterUiEvent.OnAdminPinChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.ADMIN_CODE) {
                        it.copy(adminCode = event.adminPIN, errors = it.errors removed FieldType.ADMIN_CODE)
                    } else {
                        it.copy(adminCode = event.adminPIN)
                    }
                }
            }
            is RegisterUiEvent.OnImageUriChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.IMAGE) {
                        it.copy(imageUri = event.imageUri, errors = it.errors removed FieldType.IMAGE)
                    } else {
                        it.copy(imageUri = event.imageUri)
                    }
                }
            }
            is RegisterUiEvent.OnLastnameChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.LASTNAME) {
                        it.copy(lastname = event.lastname, errors = it.errors removed FieldType.LASTNAME)
                    } else {
                        it.copy(lastname = event.lastname)
                    }
                }
            }
            is RegisterUiEvent.OnNamesChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.NAMES) {
                        it.copy(names = event.names, errors = it.errors removed FieldType.NAMES)
                    } else {
                        it.copy(names = event.names)
                    }
                }
            }
            is RegisterUiEvent.OnEmailChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.EMAIL) {
                        it.copy(email = event.email, errors = it.errors removed FieldType.EMAIL)
                    } else {
                        it.copy(email = event.email)
                    }
                }
            }
            is RegisterUiEvent.OnPasswordChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.PASSWORD) {
                        it.copy(password = event.password, errors = it.errors removed FieldType.PASSWORD)
                    } else {
                        it.copy(password = event.password)
                    }
                }
            }
            is RegisterUiEvent.OnPasswordConfirmChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.PASSWORD_CONFIRM) {
                        it.copy(passwordConfirm = event.passwordConfirm, errors = it.errors removed FieldType.PASSWORD_CONFIRM)
                    } else {
                        it.copy(passwordConfirm = event.passwordConfirm)
                    }
                }
            }
            is RegisterUiEvent.OnTermsAcceptChange -> {
                _uiState.update {
                    if (it.errors contain FieldType.TERMS) {
                        it.copy(termsAccepted = event.termsAccepted, errors = it.errors removed FieldType.TERMS)
                    } else {
                        it.copy(termsAccepted = event.termsAccepted)
                    }
                }
            }
            RegisterUiEvent.Register -> {
                if (checkForErrors()) register()
            }

        }
    }


    private fun checkForErrors(): Boolean {
        if (_uiState.value.userType == UserType.ADMIN) {
            if (_uiState.value.adminCode?.isBlank() == true) {
                _uiState.update {
                    it.copy(errors = it.errors added FieldError(FieldType.ADMIN_CODE, "Enter your administration PIN."))
                }
            } else if (_uiState.value.adminCode?.isDigitsOnly() != true) {
                _uiState.update {
                    it.copy(errors = it.errors added FieldError(FieldType.ADMIN_CODE, "Enter a valid administration PIN."))
                }
            }
        }

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

        val password = _uiState.value.password
        if (password.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.PASSWORD, "Create a password."))
            }
        } else if (password.notMatches(strongPasswordRegex)) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(
                    FieldType.PASSWORD,
                    "Create a strong password.\n" +
                            "Password should be 6 characters length and contain at least:\n" +
                            "-a capital letter\n" +
                            "-a small letter\n" +
                            "-a number\n" +
                            "-a special character\n")
                )
            }
        }

        if (_uiState.value.passwordConfirm.isBlank()) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.PASSWORD_CONFIRM, "Confirm your password."))
            }
        } else if (_uiState.value.passwordConfirm != _uiState.value.password) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.PASSWORD_CONFIRM, "Passwords do not match."))
            }
        }

        if (!_uiState.value.termsAccepted) {
            _uiState.update {
                it.copy(errors = it.errors added FieldError(FieldType.TERMS, "Read and accept the terms and conditions."))
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
        println("password : ${_uiState.value.password}")
        println("p_confirm: ${_uiState.value.passwordConfirm}")
    }
}