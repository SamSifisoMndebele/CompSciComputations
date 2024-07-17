package com.compscicomputations.core.client.di

import com.compscicomputations.core.client.auth.AuthRepository
import com.compscicomputations.core.client.auth.UserRepository
import com.compscicomputations.core.client.auth.impl.AuthRepositoryImpl
import com.compscicomputations.core.client.auth.impl.UserRepositoryImpl
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
    fun provideAuthRepository(
        client: HttpClient
    ): AuthRepository = AuthRepositoryImpl(client)

    @Provides
    @Singleton
    fun provideUserRepository(
        client: HttpClient
    ): UserRepository = UserRepositoryImpl(client)

//    @Provides
//    @Singleton
//    fun provide
}