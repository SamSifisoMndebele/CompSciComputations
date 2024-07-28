package com.compscicomputations.polish_expressions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.compscicomputations.polish_expressions.ui.PolishExpressions
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompSciComputationsTheme {
                PolishExpressions(
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}