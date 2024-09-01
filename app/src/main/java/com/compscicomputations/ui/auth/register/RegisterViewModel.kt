package com.compscicomputations.ui.auth.register

import android.net.Uri
import android.util.Log
import androidx.credentials.exceptions.CreateCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.model.remote.NewUser
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource.Companion.OtpException
import com.compscicomputations.client.utils.Image
import com.compscicomputations.client.utils.Image.Companion.asImage
import com.compscicomputations.client.utils.ScaledByteArrayUseCase
import com.compscicomputations.di.IoDispatcher
import com.compscicomputations.theme.emailRegex
import com.compscicomputations.theme.namesRegex
import com.compscicomputations.theme.strongPasswordRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val scaledByteArray: ScaledByteArrayUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun setPhotoUri(photoUri: Uri) {
        _uiState.value = _uiState.value.copy(imageUri = photoUri)
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
    fun onOtpChange(otp: String) {
        _uiState.value = _uiState.value.copy(otp = otp, otpError = null)
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

    suspend fun onSendOtp(success: () -> Unit) {
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter your email.")
            return
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid email.")
            return
        }

        _uiState.value = _uiState.value.copy(sendingOtp = true)
        try {
            authRepository.registerOtp(_uiState.value.email)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(sendingOtp = false, progressState  = ProgressState.Error(e.localizedMessage))
            return
        }
        _uiState.value = _uiState.value.copy(sendingOtp = false, otp = "")
        success()
    }

    private var job: Job? = null
    fun onRegister(savePassword: suspend (email: String, password: String) -> Unit = {_,_->}) {
        if (!fieldsAreValid()) return

        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Creating account..."))
        job = viewModelScope.launch {
            try {
                authRepository.createUser(
                    NewUser(
                        email = _uiState.value.email,
                        otp = _uiState.value.otp,
                        password = _uiState.value.password,
                        names = _uiState.value.names,
                        lastname = _uiState.value.lastname,
                        image = _uiState.value.imageUri?.let { scaledByteArray(it).asImage }
                    )
                ) { bytesSent, totalBytes ->
                    if (bytesSent == totalBytes) {
                        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Creating account..."))
                    } else {
                        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Uploading image...\n" +
                                "${(bytesSent/1024.0).roundToInt()}kB/${(totalBytes/1024.0).roundToInt()}kB"))
                    }
                }
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Done!"))
                savePassword(_uiState.value.email, _uiState.value.password)
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
            } catch (e: OtpException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle, otpError = e.localizedMessage)
            } catch (e: CreateCredentialException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Success)
                Log.w(TAG, "onRegister::savePassword", e)
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                Log.w(TAG, "onRegister", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "onRegister", e)
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    fun cancelRegister(handler: () -> Unit = {}) {
        job?.cancel()
        job?.invokeOnCompletion(true) {
            job = null
            handler()
            onProgressStateChange(ProgressState.Idle)
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
            _uiState.value = _uiState.value.copy(lastnameError = "Enter valid lastname.")
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
        if (!_uiState.value.termsAccepted) {
            _uiState.value = _uiState.value.copy(termsAcceptedError = "Read and accept the terms and conditions to continue.")
            valid = false
        }
        return valid
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}