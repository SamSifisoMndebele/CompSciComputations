package com.compscicomputations.number_systems.ui.floating_point

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FloatingPointScreen(
    padding: PaddingValues
) {
    Text(
        modifier = androidx.compose.ui.Modifier.padding(padding),
        text = "\n FloatingPointScreen"
    )
}