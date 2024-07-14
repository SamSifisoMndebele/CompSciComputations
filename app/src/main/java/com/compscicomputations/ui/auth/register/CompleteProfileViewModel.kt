package com.compscicomputations.ui.auth.register

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.compscicomputations.core.ktor_client.auth.AuthRepository.Companion.getAuthUser
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.core.ktor_client.auth.models.NewUser
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
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class CompleteProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CompleteProfileUiState())
    val uiState: StateFlow<CompleteProfileUiState> = _uiState.asStateFlow()

    init {
        val user = getAuthUser(false)
        _uiState.value = _uiState.value.copy(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName?:"",
            photoUrl = user.photoUrl,
            phone = user.phoneNumber,
        )
    }

    fun setUsertype(usertype: Usertype) {
        _uiState.value = _uiState.value.copy(usertype = usertype)
    }
    fun onAdminCodeChange(adminCode: String) {
        _uiState.value = _uiState.value.copy(adminCode = adminCode, adminCodeError = null)
    }
    fun setPhotoUri(photoUri: Uri) {
        _uiState.value = _uiState.value.copy(photoUri = photoUri)
    }
    private fun setPhotoUrl(photoUrl: String) {
        _uiState.value = _uiState.value.copy(photoUrl = photoUrl)
    }
    fun onDisplayNameChange(displayName: String) {
        _uiState.value = _uiState.value.copy(displayName = displayName, displayNameError = null)
    }
    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone, phoneError = null)
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
                userRepository.createUser(NewUser(
                    email = _uiState.value.email,
                    password = null,
                    displayName = _uiState.value.displayName,
                    photoUrl = _uiState.value.photoUrl,
                    phone = _uiState.value.phone,
                    usertype = _uiState.value.usertype,
                    adminCode = _uiState.value.adminCode,
                ))
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
        return valid
    }
}