package com.compscicomputations.ui.main.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.compscicomputations.ui.auth.UserType
import com.compscicomputations.ui.main.api.UserRepository
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
        val currentUser = auth.currentUser
        _userSignedOut.value = currentUser == null
        if (currentUser != null) {
//          currentUser?.metadata[""]?.also { _userType.value = it }
            _email.value = currentUser.email!!
            currentUser.displayName?.also { _displayName.value = it }
            currentUser.photoUrl?.also { _photoUrl.value = it }
        }
    }

    fun signOut() {
        auth.signOut()
        _userSignedOut.value = true
    }

}