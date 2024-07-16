package com.compscicomputations.plugins

import com.compscicomputations.authentication.configureAdminAuth
import com.compscicomputations.authentication.configureAuth
import com.compscicomputations.authentication.google.GoogleToken
import com.compscicomputations.services.auth.models.response.User
import io.ktor.server.application.*
import io.ktor.server.auth.*

internal fun Application.configureSecurity() {
    install(Authentication) {
//        configureAuth { validate { it } }
//        configureAdminAuth { validate { it } }


        //TODO: Put correct authentication here
        bearer {
            realm = "Access server as user."
            authenticate  {
                GoogleToken(
                    email = "email",
                    name = "name",
                    givenName = "givenName",
                    familyName = "familyName",
                    pictureUrl = "pictureUrl",
                    emailVerified = true,
                    isAdmin = false,
                )
            }
        }
        bearer("admin") {
            realm = "Access server as admin user."
            authenticate {
                GoogleToken(
                    email = "email",
                    name = "name",
                    givenName = "givenName",
                    familyName = "familyName",
                    pictureUrl = "pictureUrl",
                    emailVerified = true,
                    isAdmin = true,
                )
            }
        }
    }
}
