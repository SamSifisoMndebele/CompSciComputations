package com.compscicomputations.core.ktor_client.di

import androidx.credentials.GetCredentialRequest
import com.compscicomputations.core.ktor_client.BuildConfig
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.core.ktor_client.auth.impl.AuthRepositoryImpl
import com.compscicomputations.core.ktor_client.auth.impl.UserRepositoryImpl
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideCredentialRequest(): GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(
            GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build())
        .build()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        request: GetCredentialRequest,
        client: HttpClient
    ): AuthRepository = AuthRepositoryImpl(auth, request, client)

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
        client: HttpClient
    ): UserRepository = UserRepositoryImpl(auth, client)
}