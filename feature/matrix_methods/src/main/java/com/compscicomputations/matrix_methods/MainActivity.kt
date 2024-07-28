package com.compscicomputations.matrix_methods

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.compscicomputations.matrix_methods.ui.MatrixMethods
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompSciComputationsTheme {
                MatrixMethods(
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}