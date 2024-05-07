package com.compscicomputations.ui.auth.register

import android.net.Uri
import com.compscicomputations.ui.auth.UserType

sealed interface RegisterUiEvent {
    data class OnUserTypeChange(val userType: UserType): RegisterUiEvent
    data class OnAdminPinChange(val adminPIN: String): RegisterUiEvent
    data class OnNamesChange(val names: String): RegisterUiEvent
    data class OnLastnameChange(val lastname: String): RegisterUiEvent
    data class OnEmailChange(val email: String): RegisterUiEvent
    data class OnPasswordChange(val password: String): RegisterUiEvent
    data class OnPasswordConfirmChange(val passwordConfirm: String): RegisterUiEvent
    data class OnImageUriChange(val imageUri: Uri): RegisterUiEvent
    data class OnTermsAcceptChange(val termsAccepted: Boolean): RegisterUiEvent
    data object Register: RegisterUiEvent
}