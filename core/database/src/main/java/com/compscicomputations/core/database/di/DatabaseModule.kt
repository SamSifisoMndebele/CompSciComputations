package com.compscicomputations.core.database.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.room.Room
import com.compscicomputations.core.database.network.ConnectionState
import com.compscicomputations.core.database.network.Connectivity.currentConnectivityState
import com.compscicomputations.core.database.network.Connectivity.observeConnectivityAsFlow
import com.compscicomputations.core.database.room.LocalDatabase
import com.compscicomputations.core.database.remote.dao.AuthDao
import com.compscicomputations.core.database.remote.repo.UserRepo
import com.compscicomputations.core.database.remote.dao.impl.AuthDaoImpl
import com.compscicomputations.core.database.remote.repo.impl.UserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideUserDao(auth: FirebaseAuth, postgrest: Postgrest, storage: Storage, connectionState: ConnectionState): UserRepo =
        UserRepoImpl(auth, postgrest, storage, connectionState)

    @Provides
    @Singleton
    fun provideAuthDao(auth: FirebaseAuth, credentialManager: CredentialManager,
                       credentialRequest: GetCredentialRequest, userRepo: UserRepo
    ): AuthDao =
        AuthDaoImpl(auth, credentialManager, credentialRequest, userRepo)

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