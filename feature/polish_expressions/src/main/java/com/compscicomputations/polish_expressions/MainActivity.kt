package com.compscicomputations.polish_expressions

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.CSCActivity
import com.compscicomputations.polish_expressions.presentation.PolishExpressions
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : CSCActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
                PolishExpressions(
                    navigateUp = {
                        finish()
                    }
                )
            }
        }
    }
}