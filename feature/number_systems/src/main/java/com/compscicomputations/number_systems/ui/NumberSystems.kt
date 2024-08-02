package com.compscicomputations.number_systems.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.R
import com.compscicomputations.number_systems.data.model.AIState.Idle
import com.compscicomputations.number_systems.data.model.CurrentTab
import com.compscicomputations.number_systems.data.model.CurrentTab.BaseN
import com.compscicomputations.number_systems.data.model.CurrentTab.Complement
import com.compscicomputations.number_systems.data.model.CurrentTab.Excess
import com.compscicomputations.number_systems.data.model.CurrentTab.FloatingPoint
import com.compscicomputations.number_systems.ui.bases.BasesScreen
import com.compscicomputations.number_systems.ui.bases.BasesViewModel
import com.compscicomputations.number_systems.ui.bases.BasesViewModel.Companion.fromValue
import com.compscicomputations.number_systems.ui.complement.ComplementScreen
import com.compscicomputations.number_systems.ui.complement.ComplementViewModel
import com.compscicomputations.number_systems.ui.complement.ComplementViewModel.Companion.fromValue
import com.compscicomputations.number_systems.ui.excess.ExcessScreen
import com.compscicomputations.number_systems.ui.excess.ExcessViewModel
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointScreen
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointViewModel
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointViewModel.Companion.fromValue
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.utils.network.ConnectionState.Unavailable
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun NumberSystems(
    navigateUp: () -> Unit,
) {
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

    var showList by rememberSaveable { mutableStateOf(false) }

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
                        Icon(Icons.Outlined.Delete, contentDescription = "Clear fields")
                    }
                    IconButton(onClick = {
//                        when(currentTab) {
//                            BaseN -> TODO()
//                            Complement -> TODO()
//                            Excess -> TODO()
//                            FloatingPoint -> TODO()
//                        }
                        showList = true
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.List,
                            contentDescription = "Response List",
                        )
                    }
                },
                floatingActionButton = if (isOnlineAndValidState) {
                    {
                        val exists by when(currentTab) {
                            BaseN -> basesViewModel.aiResponseDao.exists(BaseN, basesUiState.convertFrom, basesUiState.fromValue)
                            Complement -> basesViewModel.aiResponseDao.exists(Complement, complementUiState.convertFrom, complementUiState.fromValue)
                            Excess -> basesViewModel.aiResponseDao.exists(Excess, excessUiState.convertFrom, basesUiState.fromValue)
                            FloatingPoint -> basesViewModel.aiResponseDao.exists(FloatingPoint, floatingPointUiState.convertFrom, floatingPointUiState.fromValue)
                        }.flowOn(Dispatchers.IO)
                            .collectAsStateWithLifecycle(initialValue = false)

                        ExtendedFloatingActionButton(
                            text = { Text(if (exists) "Show Steps" else "Generate Steps") },
                            icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.ic_google_gemini),
                                    contentDescription = null
                                )
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            onClick = {
                                when (currentTab) {
                                    BaseN -> basesViewModel.generateSteps()
                                    Complement -> complementViewModel.generateSteps()
                                    Excess -> excessViewModel.generateSteps()
                                    FloatingPoint -> floatingPointViewModel.generateSteps()
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
                onDismissRequest = { basesViewModel.setAiState(Idle) },
                onCancel = { basesViewModel.cancelJob() },
                onRegenerate = { basesViewModel.regenerateSteps() },
            )
            Complement -> StepsBox(
                scope = scope,
                aiState = complementUiState.aiState,
                onDismissRequest = { complementViewModel.setAiState(Idle) },
                onCancel = { complementViewModel.cancelJob() },
                onRegenerate = { complementViewModel.regenerateSteps() },
            )
            Excess -> StepsBox(
                scope = scope,
                aiState = excessUiState.aiState,
                onDismissRequest = { excessViewModel.setAiState(Idle) },
                onCancel = { excessViewModel.cancelJob() },
                onRegenerate = { excessViewModel.regenerateSteps() },
            )
            FloatingPoint -> StepsBox(
                scope = scope,
                aiState = floatingPointUiState.aiState,
                onDismissRequest = { floatingPointViewModel.setAiState(Idle) },
                onCancel = { floatingPointViewModel.cancelJob() },
                onRegenerate = { floatingPointViewModel.regenerateSteps() },
            )
        }

        when(currentTab) {
            BaseN -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = basesViewModel.responsesFlow,
                onDelete = { basesViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    basesViewModel.setFields(it)
                    basesViewModel.generateSteps()
                }
            )
            Complement -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = complementViewModel.responsesFlow,
                onDelete = { complementViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    complementViewModel.setFields(it)
                    complementViewModel.generateSteps()
                }
            )
            Excess -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = excessViewModel.responsesFlow,
                onDelete = { excessViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    excessViewModel.setFields(it)
                    excessViewModel.generateSteps()
                }
            )
            FloatingPoint -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = floatingPointViewModel.responsesFlow,
                onDelete = { floatingPointViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    floatingPointViewModel.setFields(it)
                    floatingPointViewModel.generateSteps()
                }
            )
        }
    }
}