package com.compscicomputations.plugins

import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.NewPassword
import com.compscicomputations.services.auth.models.requests.RegisterUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.utils.isEmailValid
import com.compscicomputations.utils.isPhoneValid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


internal fun Application.configureRequestValidation() {

    install(RequestValidation) {
        validate<RegisterUser> { userInfo ->
            when {
                !userInfo.email.isEmailValid() ->
                    ValidationResult.Invalid("Email is not valid.")
                userInfo.displayName.isNullOrBlank() ->
                    ValidationResult.Invalid("Names should not be empty.")
                userInfo.password != null && userInfo.password.length < 6 ->
                    ValidationResult.Invalid("Password is too short.")
                else -> ValidationResult.Valid
            }
        }
        validate<UpdateUser> { userInfo ->
            when {
                !userInfo.email.isEmailValid() ->
                    ValidationResult.Invalid("Email is not valid.")
                userInfo.displayName.isNullOrBlank() ->
                    ValidationResult.Invalid("Names should not be empty.")
                userInfo.password != null && userInfo.password.length < 6 ->
                    ValidationResult.Invalid("Password is too short.")
                userInfo.phone != null && !userInfo.phone.isPhoneValid() ->
                    ValidationResult.Invalid("Phone number is not valid.")
                userInfo.isAdmin == true && userInfo.adminPin.isNullOrBlank() ->
                    ValidationResult.Invalid("Admin Pin is required to verify the admin account.")
                userInfo.isStudent == true && userInfo.course.isNullOrBlank() ->
                    ValidationResult.Invalid("Course name is required for students.")
                userInfo.isStudent == true && userInfo.school.isNullOrBlank() ->
                    ValidationResult.Invalid("School name is required for students.")
                else -> ValidationResult.Valid
            }
        }
        validate<NewAdminPin> { request ->
            when {
                !request.email.isEmailValid() -> ValidationResult.Invalid("The email is not valid.")
                else -> ValidationResult.Valid
            }
        }
        validate<NewPassword> { request ->
            when {
                !request.email.isEmailValid() -> ValidationResult.Invalid("The email is not valid.")
                request.password.isBlank() -> ValidationResult.Invalid("Password is blank.")
                request.otp.isEmailValid() && request.oldPassword.isNullOrBlank() ->
                    ValidationResult.Invalid("OTP and old password are both null.")
                else -> ValidationResult.Valid
            }
        }
    }
}