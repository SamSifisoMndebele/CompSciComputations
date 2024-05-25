package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.data.repository.UserRepository
import com.compscicomputations.ui.auth.UserType
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val userRepository: UserRepository
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
        val isUserSigned = userRepository.isUserSigned()
        _userSignedOut.value = !isUserSigned
        viewModelScope.launch {
            try {
                val user = userRepository.getUser()
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
        userRepository.logout()
            .addOnSuccessListener {
                _userSignedOut.value = true
            }
    }

}