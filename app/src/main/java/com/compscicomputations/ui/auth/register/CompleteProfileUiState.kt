package com.compscicomputations.ui.auth.register

import android.net.Uri
import com.compscicomputations.core.ktor_client.auth.models.NewUser
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.compscicomputations.ui.utils.ProgressState

data class CompleteProfileUiState(
    val uid: String = "",
    val email: String = "",

    val usertype: Usertype = Usertype.STUDENT,
    val adminCode: String? = null,
    val photoUri: Uri? = null,
    val photoUrl: String? = null,
    val displayName: String = "",
    val phone: String? = null,

    val adminCodeError: String? = null,
    val displayNameError: String? = null,
    val phoneError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isAdmin: Boolean get() = usertype == Usertype.ADMIN

    val isValid: Boolean
        get() = displayName.isNotBlank() && email.isNotBlank() &&
                (!isAdmin || isAdmin && adminCode?.isNotBlank() == true)

    val asNewUser: NewUser
        get() = NewUser(
            email = email,
            password = null,
            displayName = displayName,
            photoUrl = photoUrl,
            phone = phone,
            usertype = usertype,
            adminCode = adminCode
        )
}