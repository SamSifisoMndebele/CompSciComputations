package com.compscicomputations.client.di

import com.compscicomputations.client.publik.OnboardingRepository
import com.compscicomputations.client.publik.impl.OnboardingRepositoryImpl
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