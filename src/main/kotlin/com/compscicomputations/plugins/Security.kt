package com.compscicomputations.plugins

import com.compscicomputations.firebase.firebase
import com.compscicomputations.firebase.firebaseAdmin
import io.ktor.server.application.*
import io.ktor.server.auth.*

internal fun Application.configureSecurity() {
    install(Authentication) {
        firebase { validate { it } }
        firebaseAdmin { validate { it } }
    }
}
