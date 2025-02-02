package com.compscicomputations.di

import android.content.Context
import androidx.room.Room
import com.compscicomputations.data.source.local.AiDatabase
import com.compscicomputations.data.source.local.AiResponseDao
import com.compscicomputations.ui.main.dynamic_feature.GenerateContentUseCase
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeaturesModules {
    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro", //flash
        apiKey = "AIzaSyAL75MjUu3gFqgw9yibD8sWhiy6dTS-NLk",//BuildConfig.GENERATIVE_AI_KEY,
        generationConfig = generationConfig {
            stopSequences = listOf(
                "Let me know",
            )
            temperature = 2f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        }
    )

    @Provides
    @Singleton
    fun provideGenerateContentUseCase(generativeModel: GenerativeModel): GenerateContentUseCase = GenerateContentUseCase(generativeModel)

    @Provides
    @Singleton
    fun provideAiDatabase(
        @ApplicationContext context: Context
    ): AiDatabase = Room.databaseBuilder(context, AiDatabase::class.java, "ai-responses.room.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideAiResponseDao(aiDatabase: AiDatabase): AiResponseDao = aiDatabase.aiResponseDao()
}