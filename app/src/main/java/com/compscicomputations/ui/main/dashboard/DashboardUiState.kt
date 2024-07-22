package com.compscicomputations.ui.main.dashboard

import androidx.compose.material3.SnackbarHostState
import com.compscicomputations.client.auth.models.DynamicFeature
import com.compscicomputations.ui.utils.ProgressState

data class DashboardUiState(
    val displayName: String = "",
    val email: String = "",
    val imageBytes: ByteArray? = null,
    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,

    val installedFeatures: Set<DynamicFeature>? = null,
    val showProfile: Boolean = true,

    val progressState: ProgressState = ProgressState.Loading(),
    val snackBarHostState: SnackbarHostState = SnackbarHostState(),

    val isCompleteProfile: Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DashboardUiState

        if (displayName != other.displayName) return false
        if (email != other.email) return false
        if (imageBytes != null) {
            if (other.imageBytes == null) return false
            if (!imageBytes.contentEquals(other.imageBytes)) return false
        } else if (other.imageBytes != null) return false
        if (isAdmin != other.isAdmin) return false
        if (isStudent != other.isStudent) return false
        if (installedFeatures != other.installedFeatures) return false
        if (showProfile != other.showProfile) return false
        if (progressState != other.progressState) return false
        if (snackBarHostState != other.snackBarHostState) return false
        if (isCompleteProfile != other.isCompleteProfile) return false

        return true
    }

    override fun hashCode(): Int {
        var result = displayName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + isAdmin.hashCode()
        result = 31 * result + isStudent.hashCode()
        result = 31 * result + (installedFeatures?.hashCode() ?: 0)
        result = 31 * result + showProfile.hashCode()
        result = 31 * result + progressState.hashCode()
        result = 31 * result + snackBarHostState.hashCode()
        result = 31 * result + isCompleteProfile.hashCode()
        return result
    }
}