@file:Suppress("TYPEALIAS_EXPANSION_DEPRECATION")

package com.compscicomputations.number_systems

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.compscicomputations.BuildConfig
import com.compscicomputations.number_systems.data.source.local.AiDatabase
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.ui.NumberSystems
import com.compscicomputations.number_systems.ui.bases.BasesViewModel
import com.compscicomputations.number_systems.ui.complement.ComplementViewModel
import com.compscicomputations.number_systems.ui.excess.ExcessViewModel
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointViewModel
import com.compscicomputations.theme.CompSciComputationsTheme
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.error.ApplicationAlreadyStartedException
import org.koin.dsl.module

private val appModule = module {
    single {
        GenerativeModel(
            modelName = "gemini-1.5-pro", //"gemini-1.5-flash",
            apiKey = BuildConfig.GENERATIVE_AI_KEY,
            generationConfig = GenerationConfig.Builder().apply {
                stopSequences = listOf(
                    "Let me know",
                    "However, it seems you've already provided the correct results.",
                    "there's a mistake in the provided results"
                )
            }
            .build()
        )
    }
    single { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    single { Room.databaseBuilder(get(), AiDatabase::class.java, "number-systems.room.db").build() }
    single { get<AiDatabase>().aiResponseDao() }

    viewModel { BasesViewModel(get(), get()) }
    viewModel { ComplementViewModel(get(), get()) }
    viewModel { ExcessViewModel(get()) }
    viewModel { FloatingPointViewModel(get()) }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            startKoin {
                androidLogger()
                androidContext(this@MainActivity)
                modules(appModule)
            }
        } catch (e: ApplicationAlreadyStartedException) {
            Log.w("Koin", "Error starting Koin", e)
        }
        setContent {
            CompSciComputationsTheme {
                NumberSystems(
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}