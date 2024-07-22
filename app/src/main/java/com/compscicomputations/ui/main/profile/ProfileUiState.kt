package com.compscicomputations.ui.main.profile

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import com.compscicomputations.client.auth.models.User
import com.compscicomputations.ui.utils.ProgressState

data class ProfileUiState(
    val id: String = "",
    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,
    val adminCode: String? = null,
    val photoUri: Uri? = null,
    val photoUrl: String? = null,
    val displayName: String = "",
    val email: String = "",
    val phone: String? = null,

    val user: User? = null,

    val progressState: ProgressState = ProgressState.Idle,
    val snackBarHostState: SnackbarHostState = SnackbarHostState(),
    val isSignedIn: Boolean = true,
) {
    val isValid: Boolean
        get() = displayName.isNotBlank() && email.isNotBlank() && (!isAdmin || adminCode?.isNotBlank() == true)
}