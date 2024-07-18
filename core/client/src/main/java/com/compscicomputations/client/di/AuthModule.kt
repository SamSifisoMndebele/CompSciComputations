package com.compscicomputations.client.di

import android.content.Context
import com.compscicomputations.client.auth.AuthRepository
import com.compscicomputations.client.auth.UserRepository
import com.compscicomputations.client.auth.data.source.DefaultUserRepository
import com.compscicomputations.client.auth.data.source.remote.UserNetworkDataSource
import com.compscicomputations.client.auth.impl.AuthRepositoryImpl
import com.compscicomputations.client.auth.impl.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
//    fun provideDefaultUserRepository(
//        @ApplicationContext context: Context,
//        networkDataSource: UserNetworkDataSource,
//    ) : DefaultUserRepository = DefaultUserRepository(context, networkDataSource)

//    @Provides
//    @Singleton
//    fun provide
}