package com.compscicomputations

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
