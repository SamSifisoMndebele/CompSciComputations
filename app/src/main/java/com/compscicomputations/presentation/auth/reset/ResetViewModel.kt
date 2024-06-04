package com.compscicomputations.presentation.auth.reset

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.remote.dao.AuthDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val authDao: AuthDao
): ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    var emailError by mutableStateOf<String?>(null)

    val snackBarHostState = SnackbarHostState()

    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()

    private val _emailSent = MutableStateFlow(false)
    val emailSent = _emailSent.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onSendEmail() {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                authDao.sendResetEmail(_email.value)
                _emailSent.value = true
                Log.d("ResetViewModel", "onSendEmail:success")
            } catch (e: Exception) {
                _showProgress.value = false
                Log.e("ResetViewModel", "onSendEmail:failure", e)
                e.message?.let { snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Short) }
            }
        }
    }
}