package com.compscicomputations.ui.auth.register

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePasswordResponse
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.client.auth.AuthRepository
import com.compscicomputations.theme.emailRegex
import com.compscicomputations.theme.namesRegex
import com.compscicomputations.theme.strongPasswordRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun setPhotoUri(photoUri: Uri) {
        _uiState.value = _uiState.value.copy(photoUri = photoUri)
    }
    private fun setPhotoUrl(photoUrl: String) {
        _uiState.value = _uiState.value.copy(photoUrl = photoUrl)
    }
    fun onNamesChange(names: String) {
        _uiState.value = _uiState.value.copy(names = names, namesError = null)
    }
    fun onLastnameChange(lastname: String) {
        _uiState.value = _uiState.value.copy(lastname = lastname, lastnameError = null)
    }
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }
    fun onPasswordConfirmChange(passwordConfirm: String) {
        _uiState.value = _uiState.value.copy(passwordConfirm = passwordConfirm, passwordConfirmError = null)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    private var registerJob: Job? = null
    fun onRegister(activity: Activity) {
//        if (!fieldsAreValid()) return
        val credentialManager = CredentialManager.create(activity)
        // Initialize a CreatePasswordRequest object.
        val createPasswordRequest = CreatePasswordRequest(
            id = _uiState.value.email,
            password = _uiState.value.password
        )

        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Creating account..."))
        registerJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = credentialManager.createCredential(activity, createPasswordRequest)
                        as CreatePasswordResponse
                Log.d("RegisterViewModel", "data: " + result.data)

//                authRepository.register(_uiState.value.email, _uiState.value.password)
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: Exception) {
                handleFailure(e)
            }
        }
    }

    fun cancelRegister() {
        registerJob?.cancel()
    }


    private fun handleFailure(e: Exception) {
        when (e) {
            is CancellationException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            is CreateCredentialCancellationException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            is CreateCredentialInterruptedException -> {
                // Retry-able error. Consider retrying the call.
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            is CreateCredentialException -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onLoginWithGoogle:", e)
            }
            else -> {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "Unexpected exception::onLoginWithGoogle:", e)
            }
        }
    }

    private fun fieldsAreValid(): Boolean {
        var valid = true
        if (_uiState.value.names.isBlank()) {
            _uiState.value = _uiState.value.copy(namesError = "Enter your names.")
            valid = false
        } else if (_uiState.value.names.notMatches(namesRegex)) {
            _uiState.value = _uiState.value.copy(namesError = "Enter valid names.")
            valid = false
        }
        if (_uiState.value.lastname.isBlank()) {
            _uiState.value = _uiState.value.copy(lastnameError = "Enter your lastname.")
            valid = false
        } else if (_uiState.value.lastname.notMatches(namesRegex)) {
            _uiState.value = _uiState.value.copy(lastnameError = "Enter a valid lastname.")
            valid = false
        }
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter your email.")
            valid = false
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid email.")
            valid = false
        }
        if (_uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "Create a password.")
            valid = false
        } else if (_uiState.value.password.notMatches(strongPasswordRegex)) {
            _uiState.value = _uiState.value.copy(passwordError = "Create a strong password.\n" +
                    "Password should be 6 characters length and contain at least:\n" +
                    "-a capital letter\n" +
                    "-a small letter\n" +
                    "-a number\n" +
                    "-a special character\n")
            valid = false
        }
        if (_uiState.value.passwordConfirm.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordConfirmError = "Confirm your password.")
            valid = false
        } else if (_uiState.value.passwordConfirm != _uiState.value.password) {
            _uiState.value = _uiState.value.copy(passwordConfirmError = "Passwords do not match.")
            valid = false
        }
        return valid
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}