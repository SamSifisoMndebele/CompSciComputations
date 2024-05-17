package com.compscicomputations

import com.compscicomputations.ui.main.api.UserRepository
import com.compscicomputations.ui.main.api.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth, postgrest: Postgrest) : UserRepository {
        return UserRepositoryImpl(auth, postgrest)
    }


}