package com.compscicomputations.ui.main.profile

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val snackBarHostState = SnackbarHostState()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun setProgressState(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    fun onRefresh() {
        viewModelScope.launch {
            authRepository.refreshUserFlow.first()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
                        photoUrl = user.photoUrl,
                        displayName = user.displayName,
                        progressState = ProgressState.Idle
                    )
                }
                ?: let {
                    _uiState.value = _uiState.value.copy(
                        progressState = ProgressState.Idle
                    )
                }
        }
    }

    private fun getCurrentState() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.currentUserFlow.first()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        id = user.id,
                        email = user.email,
                        phone = user.phone,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
                        photoUrl = user.photoUrl,
                        isSignedIn = true,
                        displayName = user.displayName,
                        progressState = ProgressState.Idle
                    )
                } ?: let {
                    _uiState.value = _uiState.value.copy(
                        user = null,
                        isSignedIn = false,
                        progressState = ProgressState.Idle
                    )
                }
        }
    }

    fun setPhotoUri(uri: Uri) {

    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _uiState.value = _uiState.value.copy(isSignedIn = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
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