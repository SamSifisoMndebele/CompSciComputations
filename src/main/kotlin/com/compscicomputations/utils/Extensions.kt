package com.compscicomputations.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence?.isEmailValid(): Boolean = !this.isNullOrBlank() && Regex(EMAIL_REGEX).matches(this)

fun CharSequence?.validEmailOrNull() = if (isEmailValid()) this else null

fun CharSequence.isAlphaNumeric(): Boolean = matches("[_a-zA-Z0-9]+".toRegex())
fun CharSequence.isText(): Boolean = matches("[_a-zA-Z ]+".toRegex())

fun CharSequence.isPhoneValid(): Boolean = matches(phoneNumberRegex)

suspend inline fun ApplicationCall.parameter(parameter: String): String? {
    val value = parameters[parameter]?.ifBlank { null }
    value ?: respond(HttpStatusCode.BadRequest, "${parameter.uppercase()} is blank or null")
    return value
}

private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)
inline val Timestamp.asString: String get() = toString().take(26).replace("T", " ")
//inline val Long?.asDate: Date get() = if (this == null) Date() else Date(this)
//val String.asDate: Date get() = dateFormat.parse(take(26).replace("T", " "))!!
//val Date.asString: String get() = dateFormat.format(this)

inline val Any?.OKOrNotFound: HttpStatusCode
    get() = if (this == null) HttpStatusCode.NotFound else HttpStatusCode.OK
inline val List<Any>?.OKOrNotFound: HttpStatusCode
    get() = if (isNullOrEmpty()) HttpStatusCode.NotFound else HttpStatusCode.OK
