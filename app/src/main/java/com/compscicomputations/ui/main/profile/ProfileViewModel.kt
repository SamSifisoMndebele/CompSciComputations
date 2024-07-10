package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.ui.navigation.Profile
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val snackBarHostState = SnackbarHostState()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun setProgressState(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    fun onRefresh() {
        getCurrentState()
    }

    fun setProfile(profile: Profile) {
        _uiState.value = _uiState.value.copy(
            isSignedIn = true,
            email = profile.email,
            displayName = profile.displayName,
            photoUrl = profile.photoUrl
        )
    }

    private fun getCurrentState() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
        viewModelScope.launch {
            val user = userRepository.getUser()
            Log.d("ProfileViewModel User", user.toString())
            if (user == null) {
                _uiState.value = _uiState.value.copy(isSignedIn = false)
            } else {
                _uiState.value = _uiState.value.copy(
                    user = user,
                    uid = user.uid,
                    email = user.email,
                    phone = user.phone,
                    usertype = user.usertype,
                    photoUrl = user.photoUrl,
                    isSignedIn = true,
                    displayName = user.displayName,
                    isEmailVerified = user.isEmailVerified,
                    progressState = ProgressState.Idle
                )
            }
        }
    }

    fun setPhotoUri(uri: Uri) {

    }

    fun logout() {
        _uiState.value = _uiState.value.copy(isSignedIn = false)
    }

    init {
        getCurrentState()
//        splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
//            .addOnSuccessListener {
//                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
//            }
    }
}