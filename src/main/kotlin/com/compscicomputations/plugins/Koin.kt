package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.impl.AuthServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.sql.Connection

internal fun Application.configureKoin() {
    val appModule = module {
        single<AuthService> { AuthServiceImpl() }
    }

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}