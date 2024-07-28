package com.compscicomputations.ui.main.help

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@Composable
fun HelpScreen(
    navigateUp: () -> Unit,
) {
    CompSciScaffold(
        title = "Help",
        navigateUp = navigateUp
    ) { contentPadding ->
        Text(
            modifier = Modifier.padding(contentPadding),
            text = "Feature under construction."
        )
    }
}