package com.compscicomputations.plugins

import com.compscicomputations.services.auth.models.requests.NewPassword
import com.compscicomputations.services.auth.models.requests.NewUser
import com.compscicomputations.services.auth.models.requests.UpdateUser
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.utils.isEmailValid
import com.compscicomputations.utils.isPhoneValid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


internal fun Application.configureRequestValidation() {

    install(RequestValidation) {
        validate<NewUser> { userInfo ->
            when {
                !userInfo.email.isEmailValid() ->
                    ValidationResult.Invalid("Email is not valid.")
                userInfo.names.isNullOrBlank() ->
                    ValidationResult.Invalid("Names should not be empty.")
                userInfo.lastname.isNullOrBlank() ->
                    ValidationResult.Invalid("Lastname should not be empty.")
                userInfo.password != null && userInfo.password.length < 6 ->
                    ValidationResult.Invalid("Password is too short.")
                else -> ValidationResult.Valid
            }
        }
        validate<UpdateUser> { userInfo ->
            when {
                userInfo.id.isBlank() ->
                    ValidationResult.Invalid("User id should not be empty.")
                userInfo.names.isBlank() ->
                    ValidationResult.Invalid("Names should not be empty.")
                userInfo.lastname.isBlank() ->
                    ValidationResult.Invalid("Lastname should not be empty.")
//                userInfo.password != null && userInfo.password.length < 6 ->
//                    ValidationResult.Invalid("Password is too short.")
                userInfo.phone != null && !userInfo.phone.isPhoneValid() ->
                    ValidationResult.Invalid("Phone number is not valid.")
                userInfo.isStudent && userInfo.university.isNullOrBlank() ->
                    ValidationResult.Invalid("University name is required for students.")
                userInfo.isStudent && userInfo.course.isNullOrBlank() ->
                    ValidationResult.Invalid("Course name is required for students.")
                userInfo.isStudent && userInfo.school.isNullOrBlank() ->
                    ValidationResult.Invalid("School name is required for students.")
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
        validate<NewFeedback> { request ->
            when {
                request.subject.isBlank() -> ValidationResult.Invalid("Subject is blank.")
                request.message.isBlank() -> ValidationResult.Invalid("Message is blank.")
                else -> ValidationResult.Valid
            }
        }
    }
}