package com.compscicomputations.ui.main.dashboard

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.auth.AuthRepository.Companion.getAuthUser
import com.compscicomputations.core.database.auth.UserRepository
import com.compscicomputations.core.database.auth.usecase.IsCompleteProfileUseCase
import com.compscicomputations.ui.utils.ProgressState
import com.google.android.play.core.splitinstall.SplitInstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val splitInstallManager: SplitInstallManager,
    private val isCompleteProfileUseCase: IsCompleteProfileUseCase,
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
        val authUser = getAuthUser()
        Log.d("authUser", authUser.toString())
        viewModelScope.launch {
            val features = userRepository.getUsersFeatures()
            val installedFeatures = features.toMutableSet()
            installedFeatures.retainAll { splitInstallManager.installedModules.contains(it.moduleName) }
            // Install all other features
            val otherFeature = features.toMutableSet()
            otherFeature.removeAll(installedFeatures)
            // TODO("Install all other features")

            _uiState.value = _uiState.value.copy(
                email = authUser.email,
                photoUrl = authUser.photoUrl,
                installedFeatures = installedFeatures
            )

            val usertype = authUser.usertypeFlow.single()
            if (!isCompleteProfileUseCase().single() || authUser.displayName == null || usertype == null) {
                _uiState.value = _uiState.value.copy(
                    isCompleteProfile = false,
                    progressState = ProgressState.Idle,
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    displayName = authUser.displayName!!,
                    usertype = usertype,
                    isCompleteProfile = true,
                    progressState = ProgressState.Idle,
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