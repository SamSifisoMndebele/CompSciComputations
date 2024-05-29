package com.compscicomputations.di

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSplitInstallManager(@ApplicationContext context: Context):
            SplitInstallManager = SplitInstallManagerFactory.create(context)

//    @Provides
//    @Singleton
//    fun provideTextRecogniser() = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
}