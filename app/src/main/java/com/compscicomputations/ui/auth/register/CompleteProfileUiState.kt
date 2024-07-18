package com.compscicomputations.ui.auth.register

import com.compscicomputations.client.auth.models.NewUser
import com.compscicomputations.ui.utils.ProgressState

data class CompleteProfileUiState(
    val uid: String = "",
    val email: String = "",

    val isAdmin: Boolean = false,
    val isStudent: Boolean = false,
    val adminCode: String? = null,
    val phone: String? = null,
    val termsAccepted: Boolean = false,

    val adminCodeError: String? = null,
    val phoneError: String? = null,
    val termsAcceptedError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isValid: Boolean
        get() = email.isNotBlank() && termsAccepted && (!isAdmin || adminCode?.isNotBlank() == true)

    val asNewUser: NewUser
        get() = NewUser(
            email = email,
            password = null,
            names = "displayName",
            photoUrl = "photoUrl",
            phone = phone,
            lastname = "usertype"
        )
}