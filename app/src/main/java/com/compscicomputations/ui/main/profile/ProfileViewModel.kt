package com.compscicomputations.ui.main.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.compscicomputations.data.repository.UserRepository
import com.compscicomputations.ui.auth.UserType
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        if (isUserSigned) userRepository.getUser()
            .addOnSuccessListener { user ->
                _userType.value = user.userType
                _displayName.value = user.displayName
                _email.value = user.email
                _photoUrl.value = user.photoUrl
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