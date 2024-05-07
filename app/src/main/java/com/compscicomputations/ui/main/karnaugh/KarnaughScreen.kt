package com.compscicomputations.ui.main.karnaugh

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compscicomputations.ui.main.AppBar
import com.ssmnd.karnaughmap.Karnaugh4

@Composable
fun KarnaughScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp),
    navigateUp: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        AppBar(title = "Karnaugh Maps", navigateUp = navigateUp) {
            //TODO Menu
        }
        Karnaugh4()
    }
}