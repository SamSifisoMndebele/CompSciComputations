package com.compscicomputations.core.ktor_client.di

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorClientModule {
    @Provides
    @Singleton
    fun provideHttpClient(auth: FirebaseAuth): HttpClient = HttpClient(OkHttp) {
        developmentMode = true

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "compsci-computations.onrender.com"
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
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

        install(Auth) {
            bearer {
                // Configure bearer authentication
                loadTokens {
                    // Load tokens from a local storage and return them as the 'BearerTokens' instance
                    when(val tokenResult = auth.currentUser?.getIdToken(false)?.await()) {
                        null -> null
                        else -> {
                            val accessToken = tokenResult.token
                            Log.d("accessToken", accessToken.toString())
                            accessToken?.let { BearerTokens(it, "refreshToken") }
                        }
                    }
                }
                refreshTokens { // this: RefreshTokensParams
                    // Refresh tokens and return them as the 'BearerTokens' instance
                    when(val tokenResult = auth.currentUser?.getIdToken(true)?.await()) {
                        null -> null
                        else -> {
                            val accessToken = tokenResult.token
                            Log.d("accessToken", accessToken.toString())
                            accessToken?.let { BearerTokens(it, "refreshToken") }
                        }
                    }
                }
                // Load and refresh tokens ...
//                sendWithoutRequest { request ->
//                    request.method == HttpMethod.Get ||
//                    request.url.host == "api.studentintellect.co.za"
//                }
            }
        }
    }

//    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.configureAuth(auth: FirebaseAuth) {
//
//    }
}