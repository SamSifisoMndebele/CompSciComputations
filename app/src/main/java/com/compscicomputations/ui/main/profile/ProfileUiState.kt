package com.compscicomputations.ui.main.profile

import android.net.Uri
import com.compscicomputations.core.ktor_client.auth.models.User
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.compscicomputations.ui.utils.ProgressState
import java.util.Date

data class ProfileUiState(
    val uid: String = "",
    val usertype: Usertype = Usertype.STUDENT,
    val adminCode: String? = null,
    val photoUri: Uri? = null,
    val photoUrl: String? = null,
    val displayName: String = "",
    val email: String = "",
    val phone: String? = null,

    val isEmailVerified: Boolean = false,
    val createdAt: Date = Date(),
    val user: User? = null,

    val progressState: ProgressState = ProgressState.Idle,
    val isSignedIn: Boolean = true,
) {
    val isAdmin: Boolean
        get() = usertype == Usertype.ADMIN

    val isValid: Boolean
        get() = displayName.isNotBlank() && email.isNotBlank() &&
                 (!isAdmin || isAdmin && adminCode?.isNotBlank() == true)
}