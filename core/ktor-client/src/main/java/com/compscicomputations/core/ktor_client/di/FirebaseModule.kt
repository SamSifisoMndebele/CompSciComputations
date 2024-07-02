package com.compscicomputations.core.ktor_client.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.compscicomputations.core.ktor_client.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context):
            CredentialManager = CredentialManager.create(context)

    @Provides
    @Singleton
    @Named("login")
    fun provideLoginCredentialRequest():
            GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build())
                .build()

    @Provides
    @Singleton
    @Named("register")
    fun provideRegisterCredentialRequest():
            GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build())
                .build()
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig
}