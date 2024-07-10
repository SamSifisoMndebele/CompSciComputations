package com.compscicomputations.ui.auth.register

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.compscicomputations.core.ktor_client.auth.models.Usertype
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
//    private val loginUseCase: LoginUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun setUsertype(usertype: Usertype) {
        _uiState.value = _uiState.value.copy(usertype = usertype)
    }
    fun onAdminCodeChange(adminCode: String) {
        _uiState.value = _uiState.value.copy(adminCode = adminCode, adminCodeError = null)
    }
    fun setPhotoUri(photoUri: Uri) {
        _uiState.value = _uiState.value.copy(photoUri = photoUri)
    }
    fun setPhotoUrl(photoUrl: String) {
        _uiState.value = _uiState.value.copy(photoUrl = photoUrl)
    }
    fun onDisplayNameChange(displayName: String) {
        _uiState.value = _uiState.value.copy(displayName = displayName, displayNameError = null)
    }
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }
    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone, phoneError = null)
    }
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }
    fun onPasswordConfirmChange(passwordConfirm: String) {
        _uiState.value = _uiState.value.copy(passwordConfirm = passwordConfirm, passwordConfirmError = null)
    }
    fun setTermsAccepted(termsAccepted: Boolean) {
        _uiState.value = _uiState.value.copy(termsAccepted = termsAccepted)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    private var registerJob: Job? = null
    fun onRegister() {
        if (!fieldsAreValid()) return
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Creating account..."))
        registerJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.register()
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.e("RegisterViewModel", "onRegister:", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e("RegisterViewModel", "onRegister:", e)
            }
        }
    }

    fun cancelRegister() {
        registerJob?.cancel()
    }

    private fun fieldsAreValid(): Boolean {
        var valid = true
        if (_uiState.value.usertype == Usertype.ADMIN) {
            if (_uiState.value.adminCode.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(adminCodeError = "Enter your administration PIN.")
                valid = false
            } else if (!_uiState.value.adminCode!!.isDigitsOnly()){
                _uiState.value = _uiState.value.copy(adminCodeError = "Enter a valid administration PIN.")
                valid = false
            }
        }
        if (_uiState.value.displayName.isBlank()) {
            _uiState.value = _uiState.value.copy(adminCodeError = "Enter your full names.")
            valid = false
        } else if (_uiState.value.displayName.notMatches(namesRegex)) {
            _uiState.value = _uiState.value.copy(adminCodeError = "Enter valid full names.")
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
}