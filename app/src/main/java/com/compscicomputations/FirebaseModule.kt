package com.compscicomputations

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideCredentialRequest():
            GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build())
                .build()
    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth


//    @Provides
//    @Singleton
//    fun provideFirebaseFirestore() = Firebase.firestore

}