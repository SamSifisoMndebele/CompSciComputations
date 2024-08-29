package com.compscicomputations

import com.compscicomputations.services.auth.models.response.User
import com.compscicomputations.services.publik.models.requests.NewFeedback
import com.compscicomputations.utils.Image
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import java.io.File
import kotlin.time.Duration.Companion.minutes

/*suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(HttpTimeout) {
            requestTimeoutMillis = 2.minutes.inWholeMilliseconds
        }
    }
//    val imageBytes = File("ic_logo.png").readBytes()
//    val response = client.post("http://localhost:8080/feedback") {
//        contentType(ContentType.Application.Json)
//        setBody(NewFeedback(
//            subject = "Subject 1",
//            message = "String 1",
//            suggestion = "",
//            image = Image(imageBytes),
//            userEmail = null,
//        ))
//    }
//    when(response.status) {
//        HttpStatusCode.OK -> println("Success: "+response.bodyAsText())
//        HttpStatusCode.ExpectationFailed -> println("Error: "+response.bodyAsText())
//        else -> println("Unexpected response: "+response.bodyAsText() + ", Code: "+response.status)
//    }
//
    val encodedBasicAuth = "sams.mndebele@gmail.com:123456789".encodeBase64()
    val response = client.get("http://localhost:8080/users/me") {
        headers {
            append(HttpHeaders.Authorization, "Basic $encodedBasicAuth")
        }
    }
    when (response.status) {
        HttpStatusCode.Unauthorized -> println("UnauthorizedException: " + response.bodyAsText())
        HttpStatusCode.ExpectationFailed -> println("ExpectationFailedException: " + response.bodyAsText())
        HttpStatusCode.OK -> println(response.body<User>())
        else -> throw Exception(response.bodyAsText())
    }
}*/

/*suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
    }
    val encodedBasicAuth = "sams.mndebele@gmail.com:Mndebele@9".encodeBase64()
    val response = client.get("http://localhost:8080/users/me") {
        headers {
            append(HttpHeaders.Authorization, "Basic $encodedBasicAuth")
        }
    }
    when (response.status) {
        HttpStatusCode.Unauthorized -> println("UnauthorizedException: " + response.bodyAsText())
        HttpStatusCode.ExpectationFailed -> println("ExpectationFailedException: " + response.bodyAsText())
        HttpStatusCode.OK -> println(response.body<User>())
        else -> throw Exception(response.bodyAsText())
    }
}*/


/*
val Long.bitsLength: Int
    get() = 2.0.pow(
        floor(log(
            x = floor(log(
                x = abs(x = toDouble()),
                base = 2.0
            )) + 1,
            base = 2.0
        )) + 1
    ).toInt()


fun String.dividedBits2(vararg n: Int): String = buildString {
    var i = 0
    try {
        for (j in n.reversed()) {
            append(this@dividedBits2.substring(i, i + j))
            append(" ")
            i += j
        }
    } catch (_: Exception) {}
    append(this@dividedBits2.substring(i))
}.trim()

fun String.reversedStr(reversed: Boolean = true): String =
    if (reversed) reversed() else this
fun IntArray.reversedArr(reversed: Boolean = true): IntArray =
    if (reversed) reversed().toIntArray() else this

fun String.dividedBits(vararg n: Int, reversed: Boolean = false): String = buildList {
    var i = 0
    try {
        for (j in n.reversedArr(reversed)) {
            add(this@dividedBits.reversedStr(reversed).substring(i, i + j))
            i += j
        }
    } catch (_: Exception) {}
    add(this@dividedBits.reversedStr(reversed).substring(i))
}.joinToString(" ").reversedStr(reversed).trim()

fun String.dividedBits(size: Int): String = dividedBits(
    *IntArray(length / size) { size }
)

fun main() {
    val ones64 = buildString {
        repeat(64) {
            append("$it".takeLast(1))
        }
    }
    val ones32 = buildString {
        repeat(25) {
            append("$it".takeLast(1))
        }
    }
    val ones16 = buildString {
        repeat(16) {
            append("$it".takeLast(1))
        }
    }

    println(ones16.dividedBits(1, 5, 10))
    println(ones32.dividedBits(1, 8, 23))
//    println(ones64.dividedBits(1, 11, 52))

    println(ones16.dividedBits(8))
    println(ones32.dividedBits(8))
//    println(ones64.dividedBits(8))



//    println(java.lang.Long.parseUnsignedLong("10", 10))
}*/
