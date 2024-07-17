package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.auth.AuthRepository
import com.compscicomputations.core.database.auth.AuthRepository.Companion.getAuthUser
import com.compscicomputations.core.database.auth.UserRepository
import com.compscicomputations.core.database.auth.usecase.IsCompleteProfileUseCase
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val isCompleteProfile: IsCompleteProfileUseCase,
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

    private fun getCurrentState() {
        val authUser = getAuthUser()
        _uiState.value = _uiState.value.copy(
            email = authUser.email,
            photoUrl = authUser.photoUrl,
            displayName = authUser.displayName ?: "",
            progressState = ProgressState.Loading()
        )
        viewModelScope.launch(ioDispatcher) {
            val user = userRepository.getUser()
            Log.d("ProfileViewModel User", user.toString())
            if (user == null) {
                // TODO: Navigate to edit profile
                Log.d("ProfileViewModel", "Navigate to edit profile")
//                _uiState.value = _uiState.value.copy(isSignedIn = false)
                _uiState.value = _uiState.value.copy(
                    isSignedIn = true,
                    displayName = "Complete Profile",
                    progressState = ProgressState.Idle
                )
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
                    progressState = ProgressState.Idle
                )
            }
        }
    }

    fun setPhotoUri(uri: Uri) {

    }

    fun logout() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                try {
                    if (authRepository.logout()) _uiState.value = _uiState.value.copy(isSignedIn = false)
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                }
            }
        }
    }

    init {
        getCurrentState()
//        splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
//            .addOnSuccessListener {
//                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
//            }
    }
}