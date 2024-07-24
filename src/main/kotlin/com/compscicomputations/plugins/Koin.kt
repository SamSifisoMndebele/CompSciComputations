package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.publik.PublicService
import com.compscicomputations.services.publik.impl.PublicServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

internal fun Application.configureKoin() {
    val appModule = module {
        single { AuthService() }
        single<PublicService> { PublicServiceImpl() }
    }

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}