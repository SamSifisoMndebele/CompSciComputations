package com.compscicomputations.ui.main.dashboard

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.core.ktor_client.auth.models.DynamicFeature
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
        val authUser = userRepository.getAuthUser()
        if (authUser == null) _uiState.value = _uiState.value.copy(isSignedIn = false)
        else {
            Log.d("authUser", authUser.toString())
            viewModelScope.launch {
                val usertype = authUser.usertypeFlow.single()
                val features = userRepository.getDynamicFeatures().toMutableSet()
                features.retainAll { splitInstallManager.installedModules.contains(it.moduleName) }
                _uiState.value = _uiState.value.copy(
                    isSignedIn = true,
                    displayName = authUser.displayName,
                    email = authUser.email,
                    photoUrl = authUser.photoUrl,
                    usertype = usertype,
                    progressState = ProgressState.Idle,
                    installedFeatures = features
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