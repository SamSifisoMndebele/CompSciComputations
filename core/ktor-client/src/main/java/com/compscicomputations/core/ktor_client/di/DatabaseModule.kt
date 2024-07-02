package com.compscicomputations.core.ktor_client.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.room.Room
import com.compscicomputations.core.ktor_client.network.ConnectionState
import com.compscicomputations.core.ktor_client.network.Connectivity.currentConnectivityState
import com.compscicomputations.core.ktor_client.network.Connectivity.observeConnectivityAsFlow
import com.compscicomputations.core.ktor_client.room.LocalDatabase
import com.compscicomputations.core.ktor_client.remote.dao.AuthDao
import com.compscicomputations.core.ktor_client.remote.repo.UserRepo
import com.compscicomputations.core.ktor_client.remote.dao.impl.AuthDaoImpl
import com.compscicomputations.core.ktor_client.remote.repo.impl.UserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideUserDao(auth: FirebaseAuth, postgrest: Postgrest, storage: Storage, connectionState: ConnectionState): UserRepo =
//        UserRepoImpl(auth, postgrest, storage, connectionState)

    @Provides
    @Singleton
    fun provideAuthDao(auth: FirebaseAuth,
                       @ApplicationContext context: Context,
                       credentialManager: CredentialManager,
                       @Named("login") loginCredentialRequest: GetCredentialRequest,
                       @Named("register") registerCredentialRequest: GetCredentialRequest,
                       userRepo: UserRepo
    ): AuthDao =
        AuthDaoImpl(auth, context, credentialManager, loginCredentialRequest, registerCredentialRequest, userRepo)

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase =
        Room.databaseBuilder(context, LocalDatabase::class.java, "compscicomputations-database-test")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Singleton
    @Provides
    fun provideConnectionStateFlow(@ApplicationContext context: Context): Flow<ConnectionState> {
        return context.observeConnectivityAsFlow()
    }

    @Singleton
    @Provides
    fun provideCurrentConnectivityState(@ApplicationContext context: Context): ConnectionState {
        return context.currentConnectivityState
    }
}