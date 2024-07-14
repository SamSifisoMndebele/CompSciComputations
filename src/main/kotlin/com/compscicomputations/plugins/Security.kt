package com.compscicomputations.plugins

import com.compscicomputations.firebase.FirebasePrincipal
import com.compscicomputations.firebase.firebase
import com.compscicomputations.firebase.firebaseAdmin
import io.ktor.server.application.*
import io.ktor.server.auth.*

internal fun Application.configureSecurity() {
    install(Authentication) {
//        firebase { validate { it } }
//        firebaseAdmin { validate { it } }

        //TODO: Put correct authentication here
        bearer {
            realm = "Access server as firebase user."
            authenticate  {
                FirebasePrincipal(
                    uid = "5M6ppTUwZNgaPpcNjoPlxigETt33",
                    isAdmin = false
                )
            }
        }
        bearer("admin") {
            realm = "Access server as firebase admin user."
            authenticate {
                FirebasePrincipal(
                    uid = "5M6ppTUwZNgaPpcNjoPlxigETt33",
                    isAdmin = true
                )
            }
        }
    }
}
