package com.compscicomputations.plugins

import com.compscicomputations.authentication.authAdmin
import com.compscicomputations.authentication.authUser
import com.compscicomputations.firebase.firebase
import com.compscicomputations.firebase.firebaseAdmin
import io.ktor.server.application.*
import io.ktor.server.auth.*

internal fun Application.configureSecurity() {
    install(Authentication) {
        firebase { validate { it } }
        firebaseAdmin { validate { it } }


        authUser { validate { it } }
        authAdmin { validate { /*if (it.isAdmin) it else null*/ it } }

//        //TODO: Put correct authentication here
//        bearer {
//            realm = "Access server as firebase user."
//            authenticate  {
//                FirebasePrincipal(
//                    uid = "5M6ppTUwZNgaPpcNjoPlxigETt33",
//                    email = "email",
//                    isAdmin = false
//                )
//            }
//        }
//        bearer("admin") {
//            realm = "Access server as firebase admin user."
//            authenticate {
//                FirebasePrincipal(
//                    uid = "5M6ppTUwZNgaPpcNjoPlxigETt33",
//                    email = "email",
//                    isAdmin = true
//                )
//            }
//        }
    }


//    authentication {
//        basic(name = "basic") {
//            realm = "Ktor Server"
//            validate { credentials ->
//                if (credentials.name == credentials.password) {
//                    UserIdPrincipal(credentials.name)
//                } else {
//                    null
//                }
//            }
//        }
//
//        oauth("google") {
//            urlProvider = { "http://localhost:8080/callback" }
//            providerLookup = {
//                OAuthServerSettings.OAuth2ServerSettings(
//                    name = "google",
//                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
//                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
//                    requestMethod = HttpMethod.Post,
//                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
//                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
//                    defaultScopes = listOf("profile", "email")
//                )
//            }
//            client = HttpClient(Apache)
//        }
//    }
}
