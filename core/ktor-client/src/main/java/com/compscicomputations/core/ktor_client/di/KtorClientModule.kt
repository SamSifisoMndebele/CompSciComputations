package com.compscicomputations.core.ktor_client.di

import com.compscicomputations.core.ktor_client.auth.AuthRepository.Companion.tokenResultFlow
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
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
import kotlinx.coroutines.flow.last
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorClientModule {

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
            filter { request ->
                request.url.host.contains("ktor.io")
            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(HttpRequestRetry) {
            maxRetries = 1
            retryIf { request, response ->
                response.status == HttpStatusCode.RequestTimeout
            }
            delayMillis { retry ->
                retry * 5000L
            }
            retryOnServerErrors(maxRetries = 1)
            exponentialDelay()
        }

        configureAuth()


        install(HttpTimeout) {
            connectTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
            socketTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
            requestTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
        }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.configureAuth() {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "username", password = "password")
                }
                realm = "Access to the server"
                sendWithoutRequest { request ->
                    request.url.host == "compsci-computations.onrender.com"
                }
            }
            bearer {
                // Configure bearer authentication
                loadTokens {
                    // Load tokens from a local storage and return them as the 'BearerTokens' instance
                    val accessToken = Firebase.auth.currentUser?.tokenResultFlow()?.last()?.token
//                    Log.d("accessToken", accessToken.toString())
                    accessToken?.let { BearerTokens(it, "refreshToken") }
                }
                refreshTokens { // this: RefreshTokensParams
                    // Refresh tokens and return them as the 'BearerTokens' instance
                    val accessToken = Firebase.auth.currentUser?.tokenResultFlow(true)?.last()?.token
//                    Log.d("accessToken", accessToken.toString())
                    accessToken?.let { BearerTokens(it, "refreshToken") }
                }
                // Load and refresh tokens ...
                sendWithoutRequest { request ->
                    request.url.host == "compsci-computations.onrender.com"
                }
            }
        }
    }
}