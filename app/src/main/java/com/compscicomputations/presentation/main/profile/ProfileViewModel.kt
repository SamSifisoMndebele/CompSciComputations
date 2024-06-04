package com.compscicomputations.presentation.main.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.dao.UserDao
import com.compscicomputations.core.database.remote.dao.AuthDao
import com.compscicomputations.core.database.remote.model.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao,
    private val authDao: AuthDao
) : ViewModel() {
    private val _userSigned = MutableStateFlow(false)
    val userSigned = _userSigned.asStateFlow()

    private val _userType = MutableStateFlow(UserType.STUDENT)
    private val _displayName = MutableStateFlow<String?>(null)
    private val _email = MutableStateFlow<String?>(null)
    private val _photoUrl = MutableStateFlow<Uri?>(null)

    val userType = _userType.asStateFlow()
    val displayName = _displayName.asStateFlow()
    val email = _email.asStateFlow()
    val photoUrl = _photoUrl.asStateFlow()

    init {
        _userSigned.value = authDao.currentUser() != null
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
            withContext(Dispatchers.IO) {
                try {
                    authDao.logout()
                    _userSigned.value = true
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "logout:Exception", e)
                }
            }
        }
    }

}