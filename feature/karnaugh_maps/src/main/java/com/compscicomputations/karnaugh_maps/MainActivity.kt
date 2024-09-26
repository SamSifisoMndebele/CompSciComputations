package com.compscicomputations.karnaugh_maps

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.CSCActivity
import com.compscicomputations.karnaugh_maps.ui.KarnaughScreen
import com.compscicomputations.karnaugh_maps.ui.karnaugh4.Karnaugh4ViewModel
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : CSCActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val karnaugh4ViewModel = Karnaugh4ViewModel(
            this,
            generateContentUseCase,
            aiResponseDao,
        )

        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
                KarnaughScreen(
                    karnaugh2ViewModel = karnaugh4ViewModel,
                    karnaugh3ViewModel = karnaugh4ViewModel,
                    karnaugh4ViewModel = karnaugh4ViewModel,
                    navigateUp = { finish() },
                )
            }
        }
    }
}