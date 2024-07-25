package com.compscicomputations.ui.main.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.ui.utils.ProgressState

data class ProfileUiState(
    val id: Int = 0,
    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,
    val adminCode: String? = null,
    val photoUri: Uri? = null,
    val imageBitmap: Bitmap? = null,
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