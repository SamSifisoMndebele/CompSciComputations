package com.compscicomputations.ui.main.profile

import android.net.Uri
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.isNotBlankText
import com.compscicomputations.utils.isPhoneValid

data class ProfileUiState(
    val names: String = "",
    val lastname: String = "",
    val phone: String? = null,

    val isStudent: Boolean = false,
    val university: String? = null,
    val school: String? = null,
    val course: String? = null,

    val imageUri: Uri? = null,

    val namesError: String? = null,
    val lastnameError: String? = null,
    val phoneError: String? = null,
    val universityError: String? = null,
    val schoolError: String? = null,
    val courseError: String? = null,

    val isSignedIn: Boolean = true,
) {
    val isValid: Boolean
        get() = names.isNotBlankText() && lastname.isNotBlankText() && phone.isPhoneValid()
                && (!isStudent || university.isNotBlankText() && school.isNotBlankText() && course.isNotBlankText())

    val displayName: String
        get() = "$names $lastname".trim()
}