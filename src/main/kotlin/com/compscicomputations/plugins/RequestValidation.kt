package com.compscicomputations.plugins

import com.compscicomputations.services.auth.impl.AuthServiceImpl.Companion.ExpiredException
import com.compscicomputations.services.auth.impl.AuthServiceImpl.Companion.isAdminCodeValid
import com.compscicomputations.services.auth.models.requests.CreateAdminCodeRequest
import com.compscicomputations.services.auth.models.requests.CreateUserRequest
import com.compscicomputations.utils.isAdmin
import com.compscicomputations.utils.isEmailValid
import com.compscicomputations.utils.isPhoneValid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


internal fun Application.configureRequestValidation() {

    install(RequestValidation) {
        validate<CreateUserRequest> { userInfo ->
            when {
                !userInfo.email.isEmailValid() ->
                    ValidationResult.Invalid("Email is not valid.")
                userInfo.password.length < 6 ->
                    ValidationResult.Invalid("Password is too short.")
                userInfo.displayName.isBlank() ->
                    ValidationResult.Invalid("Display name is blank.")
                userInfo.phone != null && !userInfo.phone.isPhoneValid() ->
                    ValidationResult.Invalid("Phone number is not valid.")
                userInfo.usertype.isAdmin && userInfo.adminCode.isNullOrBlank() ->
                    ValidationResult.Invalid("Admin Code is required to verify the admin account.")
                userInfo.usertype.isAdmin && !userInfo.adminCode.isNullOrBlank() ->
                    try {
                        if (isAdminCodeValid(userInfo.email, userInfo.adminCode)) ValidationResult.Valid
                        else ValidationResult.Invalid("The admin code is not valid.")
                    } catch (e: ExpiredException) {
                        ValidationResult.Invalid(e.localizedMessage)
                    }
                else -> ValidationResult.Valid
            }
        }
        validate<CreateAdminCodeRequest> { request ->
            when {
                !request.email.isEmailValid() -> ValidationResult.Invalid("The email is not valid.")
                else -> ValidationResult.Valid
            }
        }
    }
}