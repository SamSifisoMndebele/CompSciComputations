package com.compscicomputations.ui.auth.register

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.ui.auth.UserType
import com.compscicomputations.ui.theme.emailRegex
import com.compscicomputations.ui.theme.namesRegex
import com.compscicomputations.ui.theme.strongPasswordRegex
import com.compscicomputations.utils.notMatches
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {
    private val _userType = MutableStateFlow(UserType.STUDENT)
    val userType = _userType.asStateFlow()

    private val _adminCode = MutableStateFlow("")
    val adminCode = _adminCode.asStateFlow()
    var adminCodeError by mutableStateOf<String?>(null)

    private val _displayName = MutableStateFlow("")
    val displayName = _displayName.asStateFlow()
    var displayNameError by mutableStateOf<String?>(null)

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    var emailError by mutableStateOf<String?>(null)

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    var passwordError by mutableStateOf<String?>(null)

    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm = _passwordConfirm.asStateFlow()
    var passwordConfirmError by mutableStateOf<String?>(null)

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri = _photoUri.asStateFlow()
    var photoUriError by mutableStateOf<String?>(null)

    private val _termsAccepted = MutableStateFlow(false)
    val termsAccepted = _termsAccepted.asStateFlow()

    val snackBarHostState = SnackbarHostState()
    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()
    private val _userRegistered = MutableStateFlow(false)
    val userRegistered = _userRegistered.asStateFlow()

    fun setUserType(userType: UserType) {
        _userType.value = userType
    }
    fun setAdminCode(adminCode: String) {
        _adminCode.value = adminCode
    }
    fun setDisplayName(displayName: String) {
        _displayName.value = displayName
    }
    fun setEmail(email: String) {
        _email.value = email
    }
    fun setPassword(password: String) {
        _password.value = password
    }
    fun setPasswordConfirm(passwordConfirm: String) {
        _passwordConfirm.value = passwordConfirm
    }
    fun setPhotoUri(photoUri: Uri?) {
        _photoUri.value = photoUri
    }
    fun setTermsAccepted(accepted: Boolean) {
        _termsAccepted.value = accepted
    }


    fun onRegister() {
        if (fieldsErrors()) return

        _showProgress.value = true
        val profile = userProfileChangeRequest {
            this.displayName = _displayName.value
            this.photoUri = _photoUri.value
        }
        auth.createUserWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterViewModel", "onSignIn:success")
                    val user = task.result.user!!
                    user.updateProfile(profile)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Log.d("RegisterViewModel", "onSignIn:User profile updated.")
                            } else {
                                Log.e("RegisterViewModel", "onSignIn:failure profile updated", task.exception)
                                viewModelScope.launch {
                                    task.exception?.message?.let {
                                        snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                                    }
                                }
                            }
                            _userRegistered.value = true
                        }
                } else {
                    Log.w("RegisterViewModel", "onSignIn:failure", task.exception)
                    viewModelScope.launch {
                        task.exception?.message?.let {
                            snackBarHostState.showSnackbar(it)
                        }
                    }
                    _showProgress.value = false
                }
            }
    }


    private fun fieldsErrors() : Boolean {
        var errors = false
        if (_userType.value == UserType.ADMIN) {
            if (_adminCode.value.isBlank()) {
                adminCodeError = "Enter your administration PIN."
                errors = true
            } else if (!_adminCode.value.isDigitsOnly()) {
                adminCodeError = "Enter a valid administration PIN."
                errors = true
            }
        }

        if (_displayName.value.isBlank()) {
            displayNameError = "Enter your full names."
            errors = true
        } else if (_displayName.value.notMatches(namesRegex)) {
            displayNameError = "Enter valid full names."
            errors = true
        }

        if (_email.value.isBlank()) {
            emailError = "Enter your email."
            errors = true
        } else if (_email.value.notMatches(emailRegex)) {
            emailError = "Enter a valid email."
            errors = true
        }

        if (_password.value.isBlank()) {
            passwordError = "Create a password."
            errors = true
        } else if (_password.value.notMatches(strongPasswordRegex)) {
            passwordError = "Create a strong password.\n" +
                    "Password should be 6 characters length and contain at least:\n" +
                    "-a capital letter\n" +
                    "-a small letter\n" +
                    "-a number\n" +
                    "-a special character\n"
            errors = true
        }

        if (_passwordConfirm.value.isBlank()) {
            passwordConfirmError = "Confirm your password."
            errors = true
        } else if (_passwordConfirm.value != _password.value) {
            passwordConfirmError = "Passwords do not match."
            errors = true
        }

        if (!_termsAccepted.value) {
            viewModelScope.launch {
                snackBarHostState.showSnackbar("Read and accept the terms and conditions.")
            }
            errors = true
        }
        return errors
    }
}