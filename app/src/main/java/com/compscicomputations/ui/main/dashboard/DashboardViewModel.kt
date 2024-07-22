package com.compscicomputations.ui.main.dashboard

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.play.core.splitinstall.SplitInstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val splitInstallManager: SplitInstallManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserFlow
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                }
                .singleOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
                        imageBytes = user.imageBytes,
                        displayName = user.displayName,
                        showProfile = true,
                        progressState = ProgressState.Idle
                    )
                } ?: let {
                _uiState.value = _uiState.value.copy(
                    showProfile = false,
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
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                }
                .singleOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
                        imageBytes = user.imageBytes,
                        displayName = user.displayName,
                        showProfile = true,
                        progressState = ProgressState.Idle
                    )
                }
                ?: let {
                    _uiState.value = _uiState.value.copy(
                        showProfile = false,
                        progressState = ProgressState.Idle
                    )
                }
        }
    }
}