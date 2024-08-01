package com.compscicomputations.ui.main.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.ui.utils.ProgressState

data class ProfileUiState(
    val id: Int = 0,
    val imageUri: Uri? = null,
    val imageBitmap: Bitmap? = null,

    val displayName: String = "",
    val email: String = "",
    val phone: String? = null,
    val isEmailVerified: Boolean = false,

    val isStudent: Boolean = false,
    val university: String = "",
    val school: String = "",
    val course: String = "",

    val isAdmin: Boolean = false,
    val adminPin: String? = null,

    val user: User = User(id, email, displayName, imageBitmap, phone, isAdmin, isStudent, isEmailVerified),
    val student: Student = Student(id, university, school, course),

    val displayNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val universityError: String? = null,
    val schoolError: String? = null,
    val courseError: String? = null,
    val adminPinError: String? = null,

    val progressState: ProgressState = ProgressState.Idle,
    val snackBarHostState: SnackbarHostState = SnackbarHostState(),
    val isSignedIn: Boolean = true,
) {
    val isValid: Boolean
        get() = displayName.isNotBlank() && email.isNotBlank() && (!isAdmin || adminPin?.isNotBlank() == true)

    val changed: Boolean
        get() = user != User(
            id, email, displayName, imageBitmap, phone, isAdmin, isStudent, isEmailVerified
        ) || imageUri != null || student != Student(id, university, school, course)
}