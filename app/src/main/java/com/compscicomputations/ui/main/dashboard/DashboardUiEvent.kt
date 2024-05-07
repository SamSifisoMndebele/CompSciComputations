package com.compscicomputations.ui.main.dashboard

import android.net.Uri
import com.compscicomputations.ui.auth.UserType

sealed interface DashboardUiEvent {
    data class OnUserTypeChange(val userType: UserType): DashboardUiEvent
    data class OnNamesChange(val names: String): DashboardUiEvent
    data class OnLastnameChange(val lastname: String): DashboardUiEvent
    data class OnEmailChange(val email: String): DashboardUiEvent
    data class OnImageUriChange(val imageUri: Uri): DashboardUiEvent
}