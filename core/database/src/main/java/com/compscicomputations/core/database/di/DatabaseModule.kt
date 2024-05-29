package com.compscicomputations.core.database.di

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.compscicomputations.core.database.dao.AuthDao
import com.compscicomputations.core.database.dao.UserDao
import com.compscicomputations.core.database.dao.impl.AuthDaoImpl
import com.compscicomputations.core.database.dao.impl.UserDaoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideUserDao(auth: FirebaseAuth, postgrest: Postgrest, storage: Storage):
            UserDao = UserDaoImpl(auth, postgrest, storage)

    @Provides
    @Singleton
    fun provideAuthDao(
        auth: FirebaseAuth,
        credentialManager: CredentialManager,
        credentialRequest: GetCredentialRequest,
        userDao: UserDao):
            AuthDao = AuthDaoImpl(auth, credentialManager, credentialRequest, userDao)
}