package com.compscicomputations.keyboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.keyboard.model.Key
import com.compscicomputations.keyboard.model.UtilityKey

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    key: Key,
    scaleAnimationEnabled: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (key: Key) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = animateFloatAsState(
        targetValue = if (scaleAnimationEnabled) 1.2f else 1f,
        animationSpec = tween(
            durationMillis = 10,
            easing = LinearEasing
        ),
        label = "Button Anim"
    )

    TextButton(
        onClick = {
            onClick(key)
        },
        contentPadding = contentPadding,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
            .scale(scale.value)
            .focusable(interactionSource = interactionSource)
            .padding(4.dp)
    ) {
        if (key is UtilityKey && (key == UtilityKey.Not || key == UtilityKey.Or)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(
                    painterResource(key.iconId),
                    contentDescription = key.value,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = " "+key.value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else if (key is UtilityKey) {
            Icon(
                painterResource(key.iconId),
                contentDescription = key.value,
                modifier = Modifier.size(32.dp)
            )
        } else {
            Text(
                text = key.value,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}