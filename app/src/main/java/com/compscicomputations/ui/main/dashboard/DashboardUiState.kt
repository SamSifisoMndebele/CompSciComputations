package com.compscicomputations.ui.main.dashboard

import android.graphics.Bitmap
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.compscicomputations.ui.utils.ProgressState

data class DashboardUiState(
    val displayName: String = "",
    val email: String = "",
    val imageBitmap: Bitmap? = null,
    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,

    val installedFeatures: Set<DynamicFeature>? = null,
    val notInstalledFeatures: Set<DynamicFeature>? = null,

    val downloadingModule: String? = null,
    val installingModule: String? = null,
    val downloadProgress: Float = 0f,

    val progressState: ProgressState = ProgressState.Loading()
)