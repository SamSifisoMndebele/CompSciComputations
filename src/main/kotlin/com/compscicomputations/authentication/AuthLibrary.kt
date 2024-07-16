package com.compscicomputations.authentication

import io.ktor.server.auth.*
import io.ktor.server.routing.*

private fun AuthenticationConfig.auth(name: String? = null, configure: AuthConfig.() -> Unit) {
    val config = AuthConfig(name).apply(configure)
    val provider = AuthProvider(config, name?.lowercase() == "admin")
    register(provider)
}

internal fun AuthenticationConfig.authUser(configure: AuthConfig.() -> Unit) = auth("user", configure)
internal fun AuthenticationConfig.authAdmin(configure: AuthConfig.() -> Unit) = auth("admin", configure)
internal fun Route.authenticateUser(build: Route.() -> Unit): Route =
    authenticate("user", build = build)
internal fun Route.authenticateAdmin(build: Route.() -> Unit): Route =
    authenticate("admin", build = build)
internal fun Route.authenticateUserOrAdmin(build: Route.() -> Unit): Route =
    authenticate("user", "admin", build = build)