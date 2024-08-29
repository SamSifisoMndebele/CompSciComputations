package com.compscicomputations.di

import android.content.Context
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.Connectivity.currentConnectivityState
import com.compscicomputations.utils.network.Connectivity.observeConnectivityAsFlow
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.lang.annotation.ElementType
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
//@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention
@Qualifier
annotation class MainDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @IoDispatcher
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun provideSplitInstallManager(@ApplicationContext context: Context): SplitInstallManager =
        SplitInstallManagerFactory.create(context)

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