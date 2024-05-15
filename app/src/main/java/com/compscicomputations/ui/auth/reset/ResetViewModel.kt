package com.compscicomputations.ui.auth.reset

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val auth: FirebaseAuth
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
        auth.sendPasswordResetEmail(_email.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ResetViewModel", "onSendEmail:success")
                    _emailSent.value = true
                } else {

                    Log.w("ResetViewModel", "onSendEmail:failure", task.exception)
                    viewModelScope.launch {
                        task.exception?.message?.let {
                            snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                        }
                    }
                    _showProgress.value = false
                }
            }
    }
}