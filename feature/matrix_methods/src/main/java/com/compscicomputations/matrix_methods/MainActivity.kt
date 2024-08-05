package com.compscicomputations.matrix_methods

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.CSCActivity
import com.compscicomputations.matrix_methods.ui.MatrixMethods
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : CSCActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
                MatrixMethods(
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}