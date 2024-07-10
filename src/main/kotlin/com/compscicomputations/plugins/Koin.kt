package com.compscicomputations.plugins

import com.compscicomputations.services.auth.AuthService
import com.compscicomputations.services.auth.impl.AuthServiceImpl
import com.compscicomputations.services.other.SubscriptionService
import com.compscicomputations.services.other.impl.SubscriptionServiceImpl
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.io.InputStream
import java.sql.Connection

internal fun Application.configureKoin() {
    val appModule = module {
        single<FirebaseApp> {
            val serviceAccount: InputStream? =
                this::class.java.classLoader.getResourceAsStream("compsci-computations-firebase-adminsdk.json")
            val firebaseOptions = FirebaseOptions.builder().run {
                val credentials = GoogleCredentials.fromStream(serviceAccount)
                setCredentials(credentials)
                build()
            }
            FirebaseApp.initializeApp(firebaseOptions)
        }
        single<FirebaseAuth> { FirebaseAuth.getInstance(get()) }
        single<AuthService> { AuthServiceImpl(get()) }
        single<SubscriptionService> { SubscriptionServiceImpl() }
        single<Connection> { connectToPostgres() }
    }

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}