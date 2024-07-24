package com.compscicomputations.ui.main.dashboard

import android.graphics.Bitmap
import androidx.compose.material3.SnackbarHostState
import com.compscicomputations.client.auth.data.model.DynamicFeature
import com.compscicomputations.ui.utils.ProgressState

data class DashboardUiState(
    val displayName: String = "",
    val email: String = "",
    val imageBitmap: Bitmap? = null,
    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,

    val installedFeatures: Set<DynamicFeature>? = null,
    val showProfile: Boolean = true,

    val progressState: ProgressState = ProgressState.Loading(),
    val snackBarHostState: SnackbarHostState = SnackbarHostState(),

    val isCompleteProfile: Boolean = true
)