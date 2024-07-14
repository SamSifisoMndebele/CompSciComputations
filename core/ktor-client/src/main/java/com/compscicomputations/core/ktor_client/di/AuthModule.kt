package com.compscicomputations.core.ktor_client.di

import android.content.Context
import androidx.credentials.GetCredentialRequest
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Named
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
        request: GetCredentialRequest,
        client: HttpClient
    ): AuthRepository = AuthRepositoryImpl(request, client)

    @Provides
    @Singleton
    fun provideUserRepository(
        client: HttpClient
    ): UserRepository = UserRepositoryImpl(client)

//    @Provides
//    @Singleton
//    fun provide
}