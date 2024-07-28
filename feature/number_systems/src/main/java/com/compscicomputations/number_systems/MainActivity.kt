package com.compscicomputations.number_systems

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.compscicomputations.BuildConfig
import com.compscicomputations.number_systems.ui.NumberSystems
import com.compscicomputations.number_systems.ui.bases.BasesViewModel
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
    single<GenerativeModel> {
        GenerativeModel(
            modelName = "gemini-1.5-flash", //"gemini-pro-vision",
            apiKey = BuildConfig.GENERATIVE_AI_KEY,
            generationConfig = GenerationConfig
                .Builder().apply {
                    stopSequences = listOf(
                        "Let me know"
                    )
                }
                .build()
        )
    }
    single<TextRecognizer> {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    viewModel { BasesViewModel(get(), get()) }
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