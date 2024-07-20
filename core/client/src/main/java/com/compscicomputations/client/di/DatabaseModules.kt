package com.compscicomputations.client.di

import android.content.Context
import androidx.room.Room
import com.compscicomputations.client.publik.data.source.local.OnboardingItemDao
import com.compscicomputations.client.publik.data.source.local.RoomPublicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {

    @Provides
    @Singleton
    fun providePublicDatabase(@ApplicationContext context: Context): RoomPublicDatabase = Room.databaseBuilder(
        context,
        RoomPublicDatabase::class.java,
        "public.room.db"
    ).build()

    @Provides
    @Singleton
    fun provideOnboardingItemDao(database: RoomPublicDatabase): OnboardingItemDao = database.onboardingItemDao()


    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        developmentMode = true

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "compsci-computations.onrender.com"
            }
        }
        install(ContentNegotiation) { json() }
        install(Resources)
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
//            filter { request ->
//                request.url.host.contains("ktor.io")
//            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(HttpRequestRetry) {
            maxRetries = 1
            retryIf { request, response ->
                response.status == HttpStatusCode.GatewayTimeout ||
                response.status == HttpStatusCode.RequestTimeout
            }
//            delayMillis { retry ->
//                retry * 5000L
//            }
//            retryOnServerErrors(maxRetries = 1)
            exponentialDelay()
        }
        install(Auth)
        install(HttpTimeout) {
            connectTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
            socketTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            requestTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
        }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.installAuth(context: Context) {
        install(Auth) {
//            bearer {
//                loadTokens {
//                    context.idTokenCredentialsFlow.first()
//                        ?.let {
//                            Log.d("IdToken", it)
//                            BearerTokens(it, "refreshToken")
//                        }
//                }
//                sendWithoutRequest { request ->
//                    request.url.pathSegments == listOf("users", "google")
//                }
//            }
//            basic {
//                credentials {
//                    context.passwordCredentialsFlow.first()
//                        ?.let {
//                            Log.d("PasswordCredentials", it.toString())
//                            BasicAuthCredentials(username = it.email, password = it.password)
//                        }
//                }
//                sendWithoutRequest { true }
//            }
        }
    }

}