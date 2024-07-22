package com.compscicomputations.ui.main.dashboard

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.play.core.splitinstall.SplitInstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val splitInstallManager: SplitInstallManager,
) : ViewModel() {
    val snackBarHostState = SnackbarHostState()

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
        viewModelScope.launch {
            authRepository.refreshUserFlow.first()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
//                        photoUrl = user.photoUrl,
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

    private fun getCurrentState() {
        viewModelScope.launch {
//            val installedFeatures = features.toMutableSet()
//            installedFeatures.retainAll { splitInstallManager.installedModules.contains(it.moduleName) }
//            // Install all other features
//            val otherFeature = features.toMutableSet()
//            otherFeature.removeAll(installedFeatures)

            authRepository.currentUserFlow.first()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(
                        email = user.email,
                        isAdmin = user.isAdmin,
                        isStudent = user.isStudent,
//                        photoUrl = user.photoUrl,
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

    init {
        getCurrentState()
//        splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
//            .addOnSuccessListener {
//                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
//            }
    }
}