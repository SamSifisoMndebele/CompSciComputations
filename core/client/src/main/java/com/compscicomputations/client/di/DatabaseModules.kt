package com.compscicomputations.client.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.auth.data.source.local.UserDataStore.Companion.basicAuthCredentialsFlow
import com.compscicomputations.client.auth.data.source.local.UserDataStore.Companion.bearerCredentialsFlow
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
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
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
    fun provideHttpClient(@ApplicationContext context: Context): HttpClient = HttpClient(OkHttp) {
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
        installAuth(context)
        install(HttpTimeout) {
            connectTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
            socketTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
            requestTimeoutMillis //= HttpTimeout.INFINITE_TIMEOUT_MS
        }
    }

    private fun <T: HttpClientEngineConfig> HttpClientConfig<T>.installAuth(context: Context) {
        install(Auth) {
            bearer {
                loadTokens {
                    context.bearerCredentialsFlow.first()
                        ?.let {
                            Log.d("BearerTokens", "IdToken: ${it.accessToken}")
                            it
                        }
                }
                sendWithoutRequest { request ->
                    request.url.pathSegments == listOf("users", "google")
                }
            }
            basic {
                credentials {
                    context.basicAuthCredentialsFlow.first()
                        ?.let {
                            Log.d("BasicAuthCredentials", "Email: ${it.username}, Password: ${it.password}")
                            it
                        }
                }
                sendWithoutRequest { true }
            }
        }
    }

    @Singleton
    @Provides
    fun provideAuth(client: HttpClient): Auth = client.plugin(Auth)

}