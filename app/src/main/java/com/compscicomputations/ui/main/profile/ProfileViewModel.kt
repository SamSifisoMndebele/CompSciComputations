package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.UserType
import com.compscicomputations.core.database.dao.AuthDao
import com.compscicomputations.core.database.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao,
    private val authDao: AuthDao
) : ViewModel() {
    private val _userSignedOut = MutableStateFlow(false)
    val userSignedOut = _userSignedOut.asStateFlow()

    private val _userType = MutableStateFlow(UserType.STUDENT)
    private val _displayName = MutableStateFlow("Complete your profile.")
    private val _email = MutableStateFlow("")
    private val _photoUrl = MutableStateFlow<Uri?>(null)

    val userType = _userType.asStateFlow()
    val displayName = _displayName.asStateFlow()
    val email = _email.asStateFlow()
    val photoUrl = _photoUrl.asStateFlow()

    init {
        val isUserSigned = authDao.isUserSigned()
        _userSignedOut.value = !isUserSigned
        viewModelScope.launch {
            try {
                val user = userDao.getUser() ?: return@launch
                _userType.value = user.userType
                _displayName.value = user.displayName
                _email.value = user.email
                _photoUrl.value = user.photoUrl
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updateUser:Exception", e)
            }
        }

//        val emailVerified = auth.currentUser.isEmailVerified
    }

    fun logout() {
        viewModelScope.launch {
            authDao.logout()
            _userSignedOut.value = true
        }
    }

}