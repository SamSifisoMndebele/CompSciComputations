package com.compscicomputations.ui.main.dashboard

import com.compscicomputations.client.auth.models.DynamicFeature
import com.compscicomputations.ui.utils.ProgressState

data class DashboardUiState(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,

    val installedFeatures: Set<DynamicFeature>? = null,

    val progressState: ProgressState = ProgressState.Idle,

    val isCompleteProfile: Boolean = true
)