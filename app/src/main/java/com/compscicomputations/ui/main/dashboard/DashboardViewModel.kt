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
                        progressState = ProgressState.Idle
                    )
                }
                ?: let {
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error("No user signed."))
                }
        }
        val features = setOf(
            DynamicFeature(
                title = "Karnaugh Maps",
                module = "karnaugh_maps",
                icon = "ic_grid.png",
            ),
            DynamicFeature(
                title = "Number Systems",
                module = "number_systems",
                icon = "ic_number.png",
            ),
            DynamicFeature(
                title = "Polish Expressions",
                module = "polish_expressions",
                icon = "ic_abc.png"
            ),
        )
        val installedFeatures = features.toMutableSet()
        installedFeatures.retainAll { splitInstallManager.installedModules.contains(it.module) }

        _uiState.value = _uiState.value.copy(installedFeatures = installedFeatures)
    }

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
        viewModelScope.launch {
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
}