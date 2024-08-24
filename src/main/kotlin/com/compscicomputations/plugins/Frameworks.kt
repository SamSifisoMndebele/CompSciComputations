package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.publik.PublicService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single { AuthService() }
            single { PublicService() }
        })
    }
}
