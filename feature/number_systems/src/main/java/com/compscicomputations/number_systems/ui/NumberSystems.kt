package com.compscicomputations.number_systems.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.number_systems.ui.bases.BasesScreen
import com.compscicomputations.number_systems.ui.bases.BasesViewModel
import com.compscicomputations.number_systems.ui.complement.ComplementScreen
import com.compscicomputations.number_systems.ui.complement.ComplementViewModel
import com.compscicomputations.number_systems.ui.excess.ExcessScreen
import com.compscicomputations.number_systems.ui.excess.ExcessViewModel
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointScreen
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointViewModel
import com.compscicomputations.number_systems.data.model.CurrentTab
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.AIState.*
import com.compscicomputations.number_systems.data.model.CurrentTab.*
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.AnnotatedText
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.ConnectionState.*
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun NumberSystems(
    navigateUp: () -> Unit,
) {

//    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()

    var currentTab by rememberSaveable { mutableStateOf(BaseN) }

    val basesViewModel: BasesViewModel = koinViewModel()
    val complementViewModel: ComplementViewModel = koinViewModel()
    val excessViewModel: ExcessViewModel = koinViewModel()
    val floatingPointViewModel: FloatingPointViewModel = koinViewModel()

    val basesUiState by basesViewModel.uiState.collectAsStateWithLifecycle()
    val complementUiState by complementViewModel.uiState.collectAsStateWithLifecycle()
    val excessUiState by excessViewModel.uiState.collectAsStateWithLifecycle()
    val floatingPointUiState by floatingPointViewModel.uiState.collectAsStateWithLifecycle()

    val connectivityState by rememberConnectivityState()
    val isOnlineAndValidState = connectivityState.isAvailable && when(currentTab) {
        BaseN -> basesUiState.isValid
        Complement -> complementUiState.isValid
        Excess -> excessUiState.isValid
        FloatingPoint -> floatingPointUiState.isValid
    }

    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Number Systems",
        navigateUp = navigateUp,
        tabsBar = {

            if (connectivityState is Unavailable) {
                Text(
                    text = "Enable your internet connection to show steps.",
                    fontFamily = comicNeueFamily,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1C45C)),
                    color = Color.DarkGray
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        when(currentTab) {
                            BaseN -> basesViewModel.clear()
                            Complement -> complementViewModel.clear()
                            Excess -> excessViewModel.clear()
                            FloatingPoint -> floatingPointViewModel.clear()
                        }
                    }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Localized description")
                    }
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            Icons.Outlined.Favorite,
//                            contentDescription = "Localized description",
//                        )
//                    }
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            Icons.AutoMirrored.Outlined.List,
//                            contentDescription = "Localized description",
//                        )
//                    }
                },
                floatingActionButton = if (isOnlineAndValidState) {
                    {
                        ExtendedFloatingActionButton(
                            text = { Text("Show Steps") },
                            icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            onClick = {
                                when (currentTab) {
                                    BaseN -> basesViewModel.showAISteps()
                                    Complement -> complementViewModel.showAISteps()
                                    Excess -> excessViewModel.showAISteps()
                                    FloatingPoint -> floatingPointViewModel.showAISteps()
                                }
                            }
                        )
                    }
                } else null
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            PrimaryScrollableTabRow(
                selectedTabIndex = currentTab.ordinal,
                indicator = { tabPositions ->
                    if (currentTab.ordinal < tabPositions.size) {
                        val width by animateDpAsState(
                            targetValue = tabPositions[currentTab.ordinal].contentWidth,
                            label = "TabAnimation"
                        )
                        TabRowDefaults.PrimaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
                            width = width,
                            height = 6.dp,
                        )
                    }
                },
                divider = {},
            ) {
                CurrentTab.entries.forEach { tab ->
                    Tab(
                        modifier = Modifier.clip(RoundedCornerShape(22.dp)),
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        text = {
                            Text(
                                text = tab.title,
                                maxLines = 2,
                                fontSize = 14.sp,
                                fontFamily = comicNeueFamily,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            when(currentTab) {
                BaseN -> BasesScreen(
                    viewModel = basesViewModel,
                    uiState = basesUiState
                )
                Complement -> ComplementScreen(
                    viewModel = complementViewModel,
                    uiState = complementUiState
                )
                Excess -> ExcessScreen(
                    viewModel = excessViewModel,
                    uiState = excessUiState
                )
                FloatingPoint -> FloatingPointScreen(
                    viewModel = floatingPointViewModel,
                    uiState = floatingPointUiState
                )
            }
        }

        val scope = rememberCoroutineScope()
        when(currentTab) {
            BaseN -> StepsBox(
                scope = scope,
                aiState = basesUiState.aiState,
                onDismissRequest = {
                    basesViewModel.setProgressState(Idle)
                }
            )
            Complement -> StepsBox(
                scope = scope,
                aiState = complementUiState.aiState,
                onDismissRequest = {
                    complementViewModel.setProgressState(Idle)
                }
            )
            Excess -> StepsBox(
                scope = scope,
                aiState = excessUiState.aiState,
                onDismissRequest = {
                    excessViewModel.setProgressState(Idle)
                }
            )
            FloatingPoint -> StepsBox(
                scope = scope,
                aiState = floatingPointUiState.aiState,
                onDismissRequest = {
                    floatingPointViewModel.setProgressState(Idle)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepsBox(
    scope: CoroutineScope,
    aiState: AIState,
    onDismissRequest: () -> Unit
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
                                        if (!sheetState.isVisible) { onDismissRequest() }
                                    }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(text = "Cancel", fontSize = 18.sp)
                        }
                    }
                }
                is Error -> {
                    LaunchedEffect(Unit) {
                        launch(Dispatchers.IO) {
                            delay(5000)
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
                                text = aiState.message ?: "An unexpected error occurred.",
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
                    AnnotatedText(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        text = aiState.response.text
                    )
                }
                else -> {}
            }
        }
    }
}