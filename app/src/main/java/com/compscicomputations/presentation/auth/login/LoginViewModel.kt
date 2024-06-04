package com.compscicomputations.presentation.auth.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.dao.AuthDao
import com.compscicomputations.presentation.ExceptionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authDao: AuthDao
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    var emailError by mutableStateOf<String?>(null)

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    var passwordError by mutableStateOf<String?>(null)

//    val snackBarHostState = SnackbarHostState()
    private val _showProgress = MutableStateFlow(false)
    val showProgress = _showProgress.asStateFlow()

    val exceptionData = mutableStateOf<ExceptionData?>(null)

    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    private val _userLogged = MutableStateFlow(false)
    val userLogged = _userLogged.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onLogin() {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                _name.value = authDao.login(_email.value, _password.value).displayName
                _userLogged.value = true
                Log.d("LoginViewModel", "onSignIn:success")
            } catch (e: Exception) {
                exceptionData.value = ExceptionData(e.message)
                _showProgress.value = false
                Log.e("LoginViewModel", "onSignIn:failure", e)
            }
        }
    }

    fun continueWithGoogle(context: Context) {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                _name.value = authDao.googleLogin(context).displayName
                _userLogged.value = true
                Log.d("LoginViewModel", "continueWithGoogle:success")
            } catch (e: Exception) {
                if (e.message == "activity is cancelled by the user.") return@launch
                exceptionData.value = ExceptionData(e.message)
                _showProgress.value = false
                Log.e("LoginViewModel", "Exception: "+ e.message, e)
            } catch (e: Exception) {
                exceptionData.value = ExceptionData(e.message)
                _showProgress.value = false
                Log.w("LoginViewModel", "continueWithGoogle:failure", e)
            }
        }
    }

    fun onSendSignInLink() {
        viewModelScope.launch {
            try {
                authDao.sendSignInLink(_email.value)
                TODO("email sent message")
            } catch (e: Exception) {
                exceptionData.value = ExceptionData(e.message)
                _showProgress.value = false
                Log.e("LoginViewModel", "onSendSignInLink:failure", e)
            }
        }
    }

    fun onLinkLogin(emailLink: String) {
        viewModelScope.launch {
            try {
                authDao.linkLogin(_email.value, emailLink)
                _userLogged.value = true
                Log.d("LoginViewModel", "onLinkLogin:success")
            } catch (e: Exception) {
                exceptionData.value = ExceptionData(e.message)
                _showProgress.value = false
                Log.e("LoginViewModel", "onLinkLogin:failure", e)
            }
        }
    }
}