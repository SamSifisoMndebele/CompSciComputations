@file:Suppress("TYPEALIAS_EXPANSION_DEPRECATION")

package com.compscicomputations.number_systems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import com.compscicomputations.BuildConfig
import com.compscicomputations.CSCActivity
import com.compscicomputations.number_systems.data.source.local.AiDatabase
import com.compscicomputations.number_systems.data.source.local.datastore.BaseNDataStore
import com.compscicomputations.number_systems.data.source.local.datastore.ComplementDataStore
import com.compscicomputations.number_systems.data.source.local.datastore.ExcessDataStore
import com.compscicomputations.number_systems.data.source.local.datastore.FloatingPointDataStore
import com.compscicomputations.number_systems.ui.NumberSystems
import com.compscicomputations.number_systems.ui.bases.BasesViewModel
import com.compscicomputations.number_systems.ui.complement.ComplementViewModel
import com.compscicomputations.number_systems.ui.excess.ExcessViewModel
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointViewModel
import com.compscicomputations.theme.CompSciComputationsTheme
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.error.ApplicationAlreadyStartedException
import org.koin.dsl.module

private val appModule = module {
    single {
        GenerativeModel(
            modelName = "gemini-1.5-pro", //flash
            apiKey = BuildConfig.GENERATIVE_AI_KEY,
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
    }
    single {
        Room.databaseBuilder(get(), AiDatabase::class.java, "number-systems.room.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AiDatabase>().aiResponseDao() }

    single { BaseNDataStore(get()) }
    single { ComplementDataStore(get()) }
    single { ExcessDataStore(get()) }
    single { FloatingPointDataStore(get()) }

    viewModel { BasesViewModel(get(), get(), get()) }
    viewModel { ComplementViewModel(get(), get(), get()) }
    viewModel { ExcessViewModel(get(), get(), get()) }
    viewModel { FloatingPointViewModel(get(), get(), get()) }
}

class MainActivity : CSCActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(appModule)
            }
        } catch (e: ApplicationAlreadyStartedException) {
//            Log.w("Koin", "Error starting Koin", e)
        }
        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
                NumberSystems(
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}