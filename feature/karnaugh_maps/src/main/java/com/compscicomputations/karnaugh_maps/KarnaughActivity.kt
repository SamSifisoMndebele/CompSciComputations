package com.compscicomputations.karnaugh_maps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.compscicomputations.theme.CompSciComputationsTheme

class KarnaughActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompSciComputationsTheme {
                KarnaughScreen {

                }
            }
        }
    }
}