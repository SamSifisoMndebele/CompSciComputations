package com.compscicomputations.di

import android.content.Context
import android.util.Base64
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import org.intellij.lang.annotations.Language
import java.security.SecureRandom
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideGetGoogleIdOption(): GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(com.compscicomputations.core.ktor_client.BuildConfig.WEB_CLIENT_ID)
        .build()

    @Provides
    @Singleton
    fun provideGetPasswordOption(): GetPasswordOption = GetPasswordOption()

    @Provides
    @Singleton
    fun provideCredentialRequest(
        googleIdOption:GetGoogleIdOption,
        passwordOption:GetPasswordOption
    ): GetCredentialRequest =
        GetCredentialRequest(listOf(googleIdOption, passwordOption))

    @Provides
    @Singleton
    @Named("google")
    fun provideGoogleCredentialRequest(
        googleIdOption:GetGoogleIdOption
    ): GetCredentialRequest = GetCredentialRequest(listOf(googleIdOption))

    @Provides
    @Singleton
    @Named("password")
    fun providePasswordCredentialRequest(
        passwordOption:GetPasswordOption
    ): GetCredentialRequest = GetCredentialRequest(listOf(passwordOption))
}