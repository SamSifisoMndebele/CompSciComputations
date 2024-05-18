package com.compscicomputations.di

import com.compscicomputations.data.repository.UserRepository
import com.compscicomputations.data.repository.impl.UserRepositoryImpl
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
object MainModule {

    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth, postgrest: Postgrest, storage: Storage) : UserRepository {
        return UserRepositoryImpl(auth, postgrest, storage)
    }


}