package com.compscicomputations.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.dynamicfeature.InstallModuleUseCase
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
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

    val _dynamicFeatureInstall = mutableSetOf<DynamicFeatureInstall>()
    val dynamicFeatureInstall = _dynamicFeatureInstall.sortedBy { it.moduleName }

    companion object {
        private val features = setOf(
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
        _uiState.value = _uiState.value.copy(installedFeatures = installedFeatures)

        val notInstalledFeatures = features.map { DynamicFeatureInstall(module = it.module, moduleName = it.title) }.toMutableSet()
        notInstalledFeatures.removeAll { splitInstallManager.installedModules.contains(it.module) }
        _dynamicFeatureInstall.addAll(notInstalledFeatures)

    }

    data class DynamicFeatureInstall(
        val module: String = "",
        val moduleName: String = "",
        var currentProgress: Float = 0f,
        var downloading: Boolean = false,
        var failed: Boolean = false,
        var installing: Boolean = false,
        var installed: Boolean = false,
    )

    fun onInstallFeature(module: String) {
        installModule(module) { state ->
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    val item = _dynamicFeatureInstall.find { it.module == module }?.apply {
                        currentProgress = state.totalBytesToDownload().toFloat() / state.bytesDownloaded()
                        downloading = true
                        failed = false
                        installing = false
                        installed = false
                    }
                    if (item != null) {
                        _dynamicFeatureInstall.apply {
                            removeIf { it.module == module }
                            add(item)
                        }
                    }
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    val item = _dynamicFeatureInstall.find { it.module == module }?.apply {
                        downloading = false
                        failed = false
                        installing = false
                        installed = true
                    }
                    if (item != null) {
                        _dynamicFeatureInstall.apply {
                            removeIf { it.module == module }
                            add(item)
                        }
                    }
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    val item = _dynamicFeatureInstall.find { it.module == module }?.apply {
                        downloading = false
                        failed = false
                        installing = true
                        installed = false
                    }
                    if (item != null) {
                        _dynamicFeatureInstall.apply {
                            removeIf { it.module == module }
                            add(item)
                        }
                    }
                }
                SplitInstallSessionStatus.FAILED -> {
                    val item = _dynamicFeatureInstall.find { it.module == module }?.apply {
                        downloading = false
                        failed = true
                        installing = false
                        installed = false
                        currentProgress = .5f
                    }
                    if (item != null) {
                        _dynamicFeatureInstall.apply {
                            removeIf { it.module == module }
                            add(item)
                        }
                    }
                }
                SplitInstallSessionStatus.CANCELED -> {
//                    TODO()
                }
                SplitInstallSessionStatus.CANCELING -> {
//                    TODO()
                }
                SplitInstallSessionStatus.DOWNLOADED -> {
//                    TODO()
                }
                SplitInstallSessionStatus.PENDING -> {
//                    TODO()
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
//                    TODO()
                }
                SplitInstallSessionStatus.UNKNOWN -> {
//                    TODO()
                }
            }
        }
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