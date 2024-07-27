package com.compscicomputations.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.play.core.splitinstall.SplitInstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
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
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.currentUserFlow
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                }
                .firstOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
                        imageBitmap = user.imageBitmap,
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
        _uiState.value = _uiState.value.copy(
            installedFeatures = setOf(
                DynamicFeature(
                    title = "Karnaugh Maps",
                    module = "karnaugh_maps",
                    iconUrl = "https://firebasestorage.googleapis.com/v0/b/compsci-computations.appspot.com/o/icons%2Fic_grid.png?alt=media&token=b21781bd-14fd-4c93-9eab-514a79f57d61",
                    clazz = "KarnaughActivity",
                ),
                DynamicFeature(
                    title = "Number Systems",
                    module = "number_systems",
                    iconUrl = "https://firebasestorage.googleapis.com/v0/b/compsci-computations.appspot.com/o/icons%2Fic_number_64.png?alt=media&token=979b04f0-b701-42a5-a7e6-ba24b9547691",
                    clazz = "MainActivity"
                ),
                DynamicFeature(
                    title = "Polish Expressions",
                    module = "polish_expressions",
                    iconUrl = "https://firebasestorage.googleapis.com/v0/b/compsci-computations.appspot.com/o/icons%2Fic_abc.png?alt=media&token=83d5caa2-83d0-4c8a-805c-f0b74d0dc3d0"
                ),
            )
        )
    }

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.refreshUserFlow
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                }
                .firstOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
                        imageBitmap = user.imageBitmap,
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