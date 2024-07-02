package com.compscicomputations.presentation.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.ktor_client.remote.repo.UserRepo
import com.compscicomputations.core.ktor_client.remote.dao.AuthDao
import com.compscicomputations.core.ktor_client.model.Usertype
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val authDao: AuthDao
) : ViewModel() {
    private val _userSigned = MutableStateFlow(true)
    val userSigned = _userSigned.asStateFlow()

    private val _usertype = MutableStateFlow(Usertype.STUDENT)
    private val _displayName = MutableStateFlow<String?>(null)
    private val _email = MutableStateFlow<String?>(null)
    private val _photoUrl = MutableStateFlow<String?>(null)

    val userType = _usertype.asStateFlow()
    val displayName = _displayName.asStateFlow()
    val email = _email.asStateFlow()
    val photoUrl = _photoUrl.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val user = userRepo.getUser() ?: return@launch
                _usertype.value = user.getUsertype
                _displayName.value = user.metadata.displayName
                _email.value = user.email
                _photoUrl.value = user.metadata.photoUrl
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updateUser:Exception", e)
            }
        }

//        val emailVerified = auth.currentUser.isEmailVerified
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authDao.logout()
                _userSigned.value = true
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "logout:Exception", e)
            }
        }
    }

}