package com.compscicomputations.core.ktor_client.di

import com.compscicomputations.core.ktor_client.auth.AuthRepository
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.core.ktor_client.auth.impl.AuthRepositoryImpl
import com.compscicomputations.core.ktor_client.auth.impl.UserRepositoryImpl
import com.compscicomputations.core.ktor_client.publik.OnboardingRepository
import com.compscicomputations.core.ktor_client.publik.impl.OnboardingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PublicModule {

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        client: HttpClient
    ): OnboardingRepository = OnboardingRepositoryImpl(client)
}