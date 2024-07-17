package com.compscicomputations.ui.main.dashboard

import com.compscicomputations.core.database.auth.models.DynamicFeature
import com.compscicomputations.core.database.auth.models.Usertype
import com.compscicomputations.ui.utils.ProgressState

data class DashboardUiState(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val usertype: Usertype = Usertype.OTHER,

    val installedFeatures: Set<DynamicFeature>? = null,

    val progressState: ProgressState = ProgressState.Idle,

    val isCompleteProfile: Boolean = true
)