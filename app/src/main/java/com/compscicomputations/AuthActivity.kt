package com.compscicomputations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.compscicomputations.presentation.auth.AuthHostScreen
import com.compscicomputations.ui.theme.CompSciComputationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                Surface {
                    AuthHostScreen(this)
                }
            }
        }
    }
}

