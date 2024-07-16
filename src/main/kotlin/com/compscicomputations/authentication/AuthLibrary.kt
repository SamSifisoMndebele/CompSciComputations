package com.compscicomputations.authentication

import io.ktor.server.auth.*
import io.ktor.server.routing.*

internal fun AuthenticationConfig.configureAuth(name: String? = null, configure: AuthConfig.() -> Unit) {
    val config = AuthConfig(name).apply(configure)
    val provider = AuthProvider(config, name?.lowercase() == "admin")
    register(provider)
}

internal fun AuthenticationConfig.configureAdminAuth(configure: AuthConfig.() -> Unit) = configureAuth("admin", configure)

internal fun Route.authenticateAdmin(build: Route.() -> Unit): Route =
    authenticate("admin", build = build)