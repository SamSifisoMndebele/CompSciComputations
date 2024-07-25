package com.compscicomputations

import com.compscicomputations.services.auth.models.response.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*

//suspend fun main() {
//    val client = HttpClient(CIO) {
//        install(ContentNegotiation) { json() }
//    }
//    val response = client.post("http://localhost:8080/onboarding/items") {
//        contentType(ContentType.Application.Json)
//        setBody(NewOnboardingItem(
//            "CompSci Computations.",
//            "Your Digital Laboratory For Exploring The Depths Of Mathematics and Computer Science!",
//            File("autumn-welcome.jpg").readBytes()
//        ))
//    }
//
//    when(response.status) {
//        HttpStatusCode.OK -> println(response.body<List<OnboardingItem>>())
//        HttpStatusCode.ExpectationFailed -> println("Error: "+response.bodyAsText())
//        else -> println("Unexpected error")
//    }
//}

//suspend fun main() {
//    val client = HttpClient(CIO) {
//        install(ContentNegotiation) { json() }
//    }
//    val encoded = "sams.mndebele@gmail.com:Mndebele@9".encodeBase64()
//    val response = client.get("http://localhost:8080/users/me") {
//        headers {
//            append(HttpHeaders.Authorization, "Basic $encoded")
//        }
//    }
//    when (response.status) {
//        HttpStatusCode.Unauthorized -> println("UnauthorizedException: " + response.bodyAsText())
//        HttpStatusCode.ExpectationFailed -> println("ExpectationFailedException: " + response.bodyAsText())
//        HttpStatusCode.OK -> println(response.body<User>())
//        else -> throw Exception(response.bodyAsText())
//    }
//}