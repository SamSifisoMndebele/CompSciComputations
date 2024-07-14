package com.compscicomputations.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.io.File

fun Application.configureSessions() {
    /*install(Sessions) {
        // TODO: Put in environment variables only
        val secretEncryptKey = hex(System.getenv("SESSION_ENCRYPT_KEY")?:"00112233445566778899aabbccddeeff")
        val secretSignKey = hex(System.getenv("SESSION_SIGN_KEY")?:"6819b57a326945c1968f45236589")

        cookie<FirebaseUser>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }

//        cookie<FirebaseUser>("user_session", directorySessionStorage(File("build/.sessions"))) {
//            cookie.path = "/"
//            cookie.maxAgeInSeconds = 60
//        }
    }*/
}
