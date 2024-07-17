package com.compscicomputations.ui.auth.register

import com.compscicomputations.core.client.auth.models.NewUser
import com.compscicomputations.core.client.auth.models.Usertype
import com.compscicomputations.ui.utils.ProgressState

data class CompleteProfileUiState(
    val uid: String = "",
    val email: String = "",

    val usertype: Usertype = Usertype.STUDENT,
    val adminCode: String? = null,
    val phone: String? = null,
    val termsAccepted: Boolean = false,

    val adminCodeError: String? = null,
    val phoneError: String? = null,
    val termsAcceptedError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isAdmin: Boolean get() = usertype == Usertype.ADMIN

    val isValid: Boolean
        get() = email.isNotBlank() && termsAccepted &&
                (!isAdmin || isAdmin && adminCode?.isNotBlank() == true)

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