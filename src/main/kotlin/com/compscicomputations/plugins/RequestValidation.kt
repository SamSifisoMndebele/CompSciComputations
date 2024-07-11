package com.compscicomputations.plugins

import com.compscicomputations.services.auth.impl.AuthServiceImpl.Companion.ExpiredException
import com.compscicomputations.services.auth.impl.AuthServiceImpl.Companion.isAdminCodeValid
import com.compscicomputations.services.auth.models.CreateAdminCodeRequest
import com.compscicomputations.services.auth.models.CreateUserRequest
import com.compscicomputations.services.other.models.Subscription
import com.compscicomputations.utils.isEmailValid
import com.compscicomputations.utils.isPhoneValid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


internal fun Application.configureRequestValidation() {

    //TODO: Enable validation here
    /*install(RequestValidation) {
//        validate<String> { string ->
//            if (string.isBlank()) ValidationResult.Invalid("Empty uid is not accepted.")
//            else ValidationResult.Valid
//        }
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
                userInfo.isAdmin && userInfo.adminCode.isNullOrBlank() ->
                    ValidationResult.Invalid("Admin Code is required to verify the admin account.")
                userInfo.isAdmin && !userInfo.adminCode.isNullOrBlank() ->
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
        validate<Subscription> { sub ->
            when {
                !sub.email.isEmailValid() -> ValidationResult.Invalid("The email is not valid.")
                else -> ValidationResult.Valid
            }
        }
    }*/
}