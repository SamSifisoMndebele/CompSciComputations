package com.compscicomputations.ui.auth.register

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.auth.models.NewUser
import com.compscicomputations.ui.utils.ProgressState
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
class CompleteProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CompleteProfileUiState())
    val uiState: StateFlow<CompleteProfileUiState> = _uiState.asStateFlow()

    init {
//        val user = getAuthUser(false)
//        _uiState.value = _uiState.value.copy(
//            uid = user.uid,
//            email = user.email,
//            phone = user.phoneNumber,
//        )
    }

    fun setIsStudent(isStudent: Boolean) {
        _uiState.value = _uiState.value.copy(isStudent = isStudent)
    }
    fun setIsAdmin(isAdmin: Boolean) {
        _uiState.value = _uiState.value.copy(isAdmin = isAdmin)
    }
    fun onAdminCodeChange(adminCode: String) {
        _uiState.value = _uiState.value.copy(adminCode = adminCode, adminCodeError = null)
    }
    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone, phoneError = null)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    fun setTermsAccepted(termsAccepted: Boolean) {
        _uiState.value = _uiState.value.copy(termsAccepted = termsAccepted)
    }

    private var registerJob: Job? = null
    fun onRegister() {
        if (!fieldsAreValid()) return
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Creating account..."))
        registerJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.createUser(NewUser(
                    email = _uiState.value.email,
                    password = "null",
                    names = "_uiState.value.nam",
                    photoUrl = "_uiState.value.photoUrl",
                    phone = _uiState.value.phone,
                    lastname = "_uiState.value.usertype",
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
        if (_uiState.value.isAdmin) {
            if (_uiState.value.adminCode.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(adminCodeError = "Enter your administration PIN.")
                valid = false
            } else if (!_uiState.value.adminCode!!.isDigitsOnly()){
                _uiState.value = _uiState.value.copy(adminCodeError = "Enter a valid administration PIN.")
                valid = false
            }
        }
        if (!_uiState.value.termsAccepted) {
            _uiState.value = _uiState.value.copy(termsAcceptedError = "You must accept the terms and conditions.")
            valid = false
        }
        return valid
    }
}