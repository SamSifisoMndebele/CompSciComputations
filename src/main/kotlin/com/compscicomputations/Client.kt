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
//    val response = client.get("http://localhost:8080/users/google") {
//        headers {
//            append(HttpHeaders.Authorization, "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImYyZTExOTg2MjgyZGU5M2YyN2IyNjRmZDJhNGRlMTkyOTkzZGNiOGMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2NjQ3NDI4NzQ4MDItMW9oYjdoMHFuZ2FiM2RmMHNzdGVlZThiZDFsbDQ0OGkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NjQ3NDI4NzQ4MDItbjQza3FxZDNmYmFwNzEwb2k3NjI3cjM1cXZvbTZ1aG8uYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTM3NzAxMDIwMjcxMjQ0NjczNTIiLCJlbWFpbCI6InNhbXMubW5kZWJlbGVAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJTYW0gTW5kZWJlbGUiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUNnOG9jSmFpRTBTajJjYWlqZ21EUnE5TC1jMklZdktSbUgzRU9NTzRUM2tGQnRuT0U0a252bmg9czk2LWMiLCJnaXZlbl9uYW1lIjoiU2FtIiwiZmFtaWx5X25hbWUiOiJNbmRlYmVsZSIsImlhdCI6MTcyMTg0NDE2MCwiZXhwIjoxNzIxODQ3NzYwfQ.SByr3GjAipdz-kn9MZ9CXoXrRB5MoZhGU-x7aBWdvQj6OEwuOc25c9bsy5C9rLmH4yrrIwhJLVFCmeViW4z1lsJq4CfLzsm_5_Gma0YUwaZzPG-aZWvOCs4vsvGvTzTCepLswXViBe9vPT_lHRD1DA080z76c4iGcGFfMcR0VySCkRN2OdohsAtG7qH197bLTIsELJgkw-VIHpqmgyGsFGw9YMKzvyRGLhJVQQoAXoFhaSg7hN2hTn3R5Nygvh1qZrlx6qd7pZGSGYPAiHYcqPcxjc3LFjquy--YkQUfzUJQ_yvLZHbm_AedGM0L7CapFRnDJsegeK19_TJ1omXz0g")
//        }
//    }
//    when (response.status) {
//        HttpStatusCode.Unauthorized -> println("throw UnauthorizedException(response.body<String?>())")
//        HttpStatusCode.ExpectationFailed -> println("throw ExpectationFailedException(response.bodyAsText())")
//        HttpStatusCode.OK -> println(response.body<User>())
//        else -> throw Exception(response.bodyAsText())
//    }
//}