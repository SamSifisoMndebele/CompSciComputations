package com.compscicomputations.di

import android.app.Application
import android.content.Context
import com.compscicomputations.data.repository.UserRepository
import com.compscicomputations.data.repository.impl.UserRepositoryImpl
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideContext(app: Application) : Context = app.applicationContext


    @Provides
    @Singleton
    fun provideCoroutineDispatcher() :
            ExecutorCoroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()

    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth, postgrest: Postgrest, storage: Storage) :
            UserRepository = UserRepositoryImpl(auth, postgrest, storage)

    @Provides
    @Singleton
    fun provideSplitInstallManager(@ApplicationContext context: Context):
            SplitInstallManager = SplitInstallManagerFactory.create(context)

//    @Provides
//    @Singleton
//    fun provideTextRecogniser() = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
}