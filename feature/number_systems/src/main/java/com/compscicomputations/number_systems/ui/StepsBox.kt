package com.compscicomputations.number_systems.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.AIState.Error
import com.compscicomputations.number_systems.data.model.AIState.Idle
import com.compscicomputations.number_systems.data.model.AIState.Loading
import com.compscicomputations.number_systems.data.model.AIState.Success
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.AnnotatedText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsBox(
    scope: CoroutineScope = rememberCoroutineScope(),
    errorDelay: Long = 6000,
    aiState: AIState = Idle,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onRegenerate: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    if (aiState !is Idle) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            properties = ModalBottomSheetProperties(
                securePolicy = SecureFlagPolicy.Inherit,
                isFocusable = true,
                shouldDismissOnBackPress = false,
            )
        ) {
            when (aiState) {
                is Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                strokeWidth = 6.dp,
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(bottom = 8.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = aiState.message,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        TextButton(
                            onClick = {
                                scope.launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) { onCancel() }
                                    }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 8.dp)
                        ) {
                            Text(text = "Cancel", fontSize = 18.sp)
                        }
                    }
                }
                is Error -> {
                    LaunchedEffect(Unit) {
                        launch(Dispatchers.IO) {
                            delay(errorDelay)
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) { onDismissRequest() }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(200.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = aiState.e.localizedMessage ?: "An unexpected error occurred.",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        TextButton(
                            onClick = {
                                scope.launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) { onDismissRequest() }
                                    }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 8.dp)
                        ) {
                            Text(text = "Close", fontSize = 18.sp)
                        }
                    }
                }
                is Success -> {
                    val scrollState = rememberScrollState()
                    Box {
                        AnnotatedText(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                .verticalScroll(scrollState),
                            text = aiState.response.text
                        )
                        OutlinedButton(
                            onClick = onRegenerate,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp)
                        ) {
                            Text(
                                text = "Regenerate Steps",
                                fontFamily = comicNeueFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}