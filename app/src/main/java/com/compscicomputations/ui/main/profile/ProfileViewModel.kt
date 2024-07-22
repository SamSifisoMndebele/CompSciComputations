package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserFlow
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                }
                .singleOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        user = user,
                        id = user.id,
                        email = user.email,
                        phone = user.phone,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
//                        photoUrl = user.photoUrl,
                        isSignedIn = true,
                        displayName = user.displayName,
                        progressState = ProgressState.Idle,
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

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
        viewModelScope.launch {
            authRepository.refreshUserFlow
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                }
                .singleOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
//                        photoUrl = user.photoUrl,
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
}