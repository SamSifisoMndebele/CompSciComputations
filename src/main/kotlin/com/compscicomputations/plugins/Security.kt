package com.compscicomputations.plugins

import com.compscicomputations.firebase.FirebaseUser
import com.compscicomputations.firebase.firebase
import com.compscicomputations.firebase.firebaseAdmin
import io.ktor.server.application.*
import io.ktor.server.auth.*
import jdk.nashorn.internal.runtime.regexp.RegExpFactory.validate

internal fun Application.configureSecurity() {
    install(Authentication) {
//        firebase { validate { it } }
//        firebaseAdmin { validate { it } }

        //TODO: Put correct authentication here
        bearer {
            realm = "Access server as firebase user."
            authenticate  {
                FirebaseUser(
                    uid = "5M6ppTUwZNgaPpcNjoPlxigETt33",
                    email = "testEmail",
                    displayName = "testDisplayName",
                    photoUrl = null,
                    isEmailVerified = true,
                    claims = mapOf("admin" to false),
                )
            }
        }
        bearer("admin") {
            realm = "Access server as firebase admin user."
            authenticate {
                val tokenResult = FirebaseUser(
                    uid = "5M6ppTUwZNgaPpcNjoPlxigETt33",
                    email = "testAdminEmail",
                    displayName = "testAdminDisplayName",
                    photoUrl = null,
                    isEmailVerified = true,
                    claims = mapOf("admin" to true),
                )
                val firebaseToken = when {
                    tokenResult.claims["admin"] as Boolean? != true -> null
                    else -> tokenResult
                }
                firebaseToken
            }
        }
    }
}
