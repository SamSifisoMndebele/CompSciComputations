package com.ssmnd.karnaughmap.keyboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: Key = Key.A,
    onClick: (key: Key) -> Unit
) {
    Button(
        modifier = modifier,
        onClick = { onClick(key) },
        contentPadding = PaddingValues(),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(painter = painterResource(id = key.iconId), contentDescription = key.value)
        if (key is Key.Or) { Text(text = "Or") }
        else if (key is Key.Not) { Text(text = "Not") }
    }

}