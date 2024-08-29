package com.compscicomputations.ui.main.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.isNotBlankText
import com.compscicomputations.utils.isPhoneValid

data class ProfileUiState(
    val id: String = "",
    val imageUri: Uri? = null,

    val names: String = "",
    val lastname: String = "",
    val phone: String? = null,

    val isStudent: Boolean = false,
    val university: String = "",
    val school: String = "",
    val course: String = "",

    val namesError: String? = null,
    val lastnameError: String? = null,
    val phoneError: String? = null,
    val universityError: String? = null,
    val schoolError: String? = null,
    val courseError: String? = null,

    val progressState: ProgressState = ProgressState.Idle,
    val isSignedIn: Boolean = true,
) {
    val isValid: Boolean
        get() = names.isNotBlankText() && lastname.isNotBlankText() && phone.isPhoneValid()
                && (!isStudent || university.isNotBlankText() && school.isNotBlankText() && course.isNotBlankText())

    val displayName: String
        get() = "$names $lastname".trim()
}