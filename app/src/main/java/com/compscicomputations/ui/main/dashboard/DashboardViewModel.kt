package com.compscicomputations.ui.main.dashboard

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.dynamicfeature.InstallModuleUseCase
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
    private val installModule: InstallModuleUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    val snackBarHostState: SnackbarHostState = SnackbarHostState()

    companion object {
        private val features = setOf(
            DynamicFeature(
                name = "Karnaugh Maps",
                module = "karnaugh_maps",
                icon = "ic_grid.png",
            ),
            DynamicFeature(
                name = "Number Systems",
                module = "number_systems",
                icon = "ic_number.png",
            ),
            DynamicFeature(
                name = "Polish Expressions",
                module = "polish_expressions",
                icon = "ic_abc.png"
            ),
        )
    }

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
        val installedFeatures = features.toMutableSet()
        installedFeatures.retainAll { splitInstallManager.installedModules.contains(it.module) }

        val notInstalledFeatures = features.toMutableSet()
        notInstalledFeatures.removeAll(installedFeatures)

        _uiState.value = _uiState.value.copy(
            installedFeatures = installedFeatures,
            notInstalledFeatures = notInstalledFeatures
        )
    }

    fun onInstallFeature(feature: DynamicFeature) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                installModule(
                    feature.module,
                    onDownload = { bytesDownloaded, totalBytes ->
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = bytesDownloaded.toFloat() / totalBytes,
                            downloadingModule = feature.module,
                            installingModule = null,
                        )
                    },
                    onFailure = {
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = 0f,
                            downloadingModule = null,
                            installingModule = null,
                        )
                        viewModelScope.launch(Dispatchers.IO) {
                            snackBarHostState.showSnackbar(
                                "Failed to install ${feature.name}",
                                withDismissAction = true
                            )
                        }
                    },
                    onInstalling = {
                        _uiState.value = _uiState.value.copy(
                            downloadProgress = 0f,
                            downloadingModule = null,
                            installingModule = feature.module
                        )
                    }
                ) {
                    _uiState.value = _uiState.value.copy(
                        downloadProgress = 0f,
                        downloadingModule = null,
                        installingModule = null,
                        installedFeatures = _uiState.value.installedFeatures?.toMutableSet()?.apply {
                            add(feature)
                        },
                        notInstalledFeatures = _uiState.value.notInstalledFeatures?.toMutableSet()?.apply {
                            remove(feature)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel","onInstallFeature::error", e)
                _uiState.value = _uiState.value.copy(
                    downloadProgress = 0f,
                    downloadingModule = null,
                    installingModule = null,
                )
                viewModelScope.launch(Dispatchers.IO) {
                    snackBarHostState.showSnackbar(
                        "Failed to install ${feature.name}, please try again.",
                        withDismissAction = true
                    )
                }

//                repeat(1000) {
//                    delay(5)
//                    _uiState.value = _uiState.value.copy(
//                        downloadingModule = feature.module,
//                        installingModule = null,
//                        downloadProgress = it.toFloat() / 1000
//                    )
//                }
//                _uiState.value = _uiState.value.copy(
//                    downloadProgress = 0f,
//                    downloadingModule = null,
//                    installingModule = feature.module
//                )
//                delay(4000)
//                _uiState.value = _uiState.value.copy(
//                    downloadProgress = 0f,
//                    downloadingModule = null,
//                    installingModule = null,
//                    installedFeatures = _uiState.value.installedFeatures?.toMutableSet()?.apply {
//                        add(feature)
//                    },
//                    notInstalledFeatures = _uiState.value.notInstalledFeatures?.toMutableSet()?.apply {
//                        remove(feature)
//                    }
//                )
            }
        }
    }

//    fun onRefresh() {
//        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
//        viewModelScope.launch {
//            authRepository.refreshUserFlow
//                .flowOn(Dispatchers.IO)
//                .catch { e ->
//                    Log.w("DashboardViewModel", e)
//                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
//                }
//                .firstOrNull()
//                ?.let { user ->
//                    _uiState.value = _uiState.value.copy(
//                        email = user.email,
//                        isAdmin = user.isAdmin,
//                        isStudent = user.isStudent,
//                        imageBitmap = user.imageBitmap,
//                        displayName = user.displayName,
//                        progressState = ProgressState.Idle
//                    )
//                }
//                ?: let {
//                    _uiState.value = _uiState.value.copy(
//                        progressState = ProgressState.Idle
//                    )
//                }
//        }
//    }
}