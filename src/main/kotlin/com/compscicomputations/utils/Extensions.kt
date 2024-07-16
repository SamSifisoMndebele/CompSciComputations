package com.compscicomputations.utils

import com.compscicomputations.services.auth.models.other.AdminUser
import com.compscicomputations.services.auth.models.other.StudentUser
import com.compscicomputations.services.auth.models.response.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.SerialName
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun String?.isEmailValid(): Boolean = !this.isNullOrBlank() && Regex(EMAIL_REGEX).matches(this)

fun String?.validEmailOrNull() = if (isEmailValid()) this else null

fun String.isAlphaNumeric() = matches("[a-zA-Z0-9]+".toRegex())
fun String.isText() = matches("[a-zA-Z ]+".toRegex())

fun String.isPhoneValid() = matches("[0-9]{9,16}".toRegex()) //Todo set phone number regex

suspend fun ApplicationCall.parameter(parameter: String): String? {
    val value = parameters[parameter]?.ifBlank { null }
    value ?: respond(HttpStatusCode.BadRequest, "${parameter.uppercase()} is blank or null")
    return value
}

private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)
inline val Timestamp.asString: String get() = toString().take(26).replace("T", " ")
//inline val Long?.asDate: Date get() = if (this == null) Date() else Date(this)
//val String.asDate: Date get() = dateFormat.parse(take(26).replace("T", " "))!!
//val Date.asString: String get() = dateFormat.format(this)


val User.displayName: String
    get() = "$names $lastname".trim()
val AdminUser.displayName: String
    get() = "$names $lastname".trim()
val StudentUser.displayName: String
    get() = "$names $lastname".trim()