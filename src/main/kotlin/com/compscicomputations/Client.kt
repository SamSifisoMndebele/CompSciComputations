package com.compscicomputations

//import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
//import com.compscicomputations.services.publik.models.response.OnboardingItem
//import io.ktor.client.*
//import io.ktor.client.call.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.serialization.kotlinx.json.*
//import java.io.File
//
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