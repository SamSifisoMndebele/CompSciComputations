package com.compscicomputations.ui.main.dashboard

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.play.core.splitinstall.SplitInstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val splitInstallManager: SplitInstallManager,
) : ViewModel() {
    val snackBarHostState = SnackbarHostState()

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

//    fun setProgressState(progressState: ProgressState) {
//        _uiState.value = _uiState.value.copy(progressState = progressState)
//    }

    fun onRefresh() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
        getCurrentState()
    }

    private fun getCurrentState() {
//        val authUser = getAuthUser()
//        Log.d("authUser", authUser.toString())
//        viewModelScope.launch {
//            val features = userRepository.getUsersFeatures()
//            val installedFeatures = features.toMutableSet()
//            installedFeatures.retainAll { splitInstallManager.installedModules.contains(it.moduleName) }
//            // Install all other features
//            val otherFeature = features.toMutableSet()
//            otherFeature.removeAll(installedFeatures)
//            // TODO("Install all other features")
//
//            _uiState.value = _uiState.value.copy(
//                email = authUser.email,
//                photoUrl = authUser.photoUrl,
//                installedFeatures = installedFeatures
//            )
//
//            if (!isCompleteProfileUseCase().single() || authUser.displayName == null) {
//                _uiState.value = _uiState.value.copy(
//                    isCompleteProfile = false,
//                    progressState = ProgressState.Idle,
//                )
//            } else {
//                _uiState.value = _uiState.value.copy(
//                    displayName = authUser.displayName!!,
//                    isAdmin = false,
//                    isCompleteProfile = true,
//                    progressState = ProgressState.Idle,
//                )
//            }
//        }
    }
    init {
        getCurrentState()
//        splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
//            .addOnSuccessListener {
//                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
//            }
    }
}