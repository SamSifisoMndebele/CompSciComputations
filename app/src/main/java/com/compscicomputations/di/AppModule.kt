package com.compscicomputations.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.compscicomputations.BuildConfig
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.Connectivity.currentConnectivityState
import com.compscicomputations.utils.network.Connectivity.observeConnectivityAsFlow
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSplitInstallManager(@ApplicationContext context: Context): SplitInstallManager =
        SplitInstallManagerFactory.create(context)

    @Provides
    @Singleton
    fun provideGenerativeModel() = GenerativeModel(
        modelName = "gemini-1.5-flash", //"gemini-pro-vision",
        apiKey = BuildConfig.GENERATIVE_AI_KEY
    )

    @Provides
    @Singleton
    fun provideTextRecogniser() = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

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