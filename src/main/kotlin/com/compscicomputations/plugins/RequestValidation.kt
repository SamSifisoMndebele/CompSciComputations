package com.compscicomputations.plugins

import com.compscicomputations.services.auth.models.requests.NewAdminPin
import com.compscicomputations.services.auth.models.requests.RegisterUser
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
                userInfo.names.isBlank() ->
                    ValidationResult.Invalid("Names are blank.")
                userInfo.lastname.isBlank() ->
                    ValidationResult.Invalid("Last name is blank.")
                userInfo.password != null && userInfo.password.length < 6 ->
                    ValidationResult.Invalid("Password is too short.")
                userInfo.phone != null && !userInfo.phone.isPhoneValid() ->
                    ValidationResult.Invalid("Phone number is not valid.")
                userInfo.isAdmin && userInfo.adminPin.isNullOrBlank() ->
                    ValidationResult.Invalid("Admin Pin is required to verify the admin account.")
                userInfo.isStudent && userInfo.course.isNullOrBlank() ->
                    ValidationResult.Invalid("Course name is required for students.")
                userInfo.isStudent && userInfo.school.isNullOrBlank() ->
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
    }
}