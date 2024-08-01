package com.compscicomputations.number_systems.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StepsList(

) {
    Column {
        repeat(5) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = { Text("One line list item with 24x24 icon") },
                overlineContent = { Text("OVERLINE") },
                supportingContent = { Text("Secondary text that is long and perhaps goes onto another line") },
                trailingContent = { Text("meta") },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                },
            )
            HorizontalDivider()
        }
    }
}