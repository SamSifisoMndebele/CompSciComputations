package com.compscicomputations.number_systems.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.R
import com.compscicomputations.number_systems.data.model.ConvertFrom
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
import com.compscicomputations.number_systems.utils.MODULE_NAME
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.main.dynamic_feature.AIState.Idle
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.ui.StepsBox
import com.compscicomputations.ui.utils.ui.StepsList
import com.compscicomputations.utils.network.ConnectionState.Unavailable
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun NumberSystems(
    navigateUp: () -> Unit,
    basesViewModel: BasesViewModel,
    complementViewModel: ComplementViewModel,
    excessViewModel: ExcessViewModel,
    floatingPointViewModel: FloatingPointViewModel,
) {
    var currentTab by rememberSaveable { mutableStateOf(BaseN) }

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
                    IconButton(onClick = { showList = true }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.List,
                            contentDescription = "Response List",
                        )
                    }
                },
                floatingActionButton = if (isOnlineAndValidState) {
                    {
                        val exists by when(currentTab) {
                            BaseN -> basesViewModel.aiResponseDao.exists(MODULE_NAME, BaseN.name, basesUiState.convertFrom.name, basesUiState.fromValue)
                            Complement -> basesViewModel.aiResponseDao.exists(MODULE_NAME, Complement.name, complementUiState.convertFrom.name, complementUiState.fromValue)
                            Excess -> basesViewModel.aiResponseDao.exists(MODULE_NAME, Excess.name, excessUiState.convertFrom.name, basesUiState.fromValue)
                            FloatingPoint -> basesViewModel.aiResponseDao.exists(MODULE_NAME, FloatingPoint.name, floatingPointUiState.convertFrom.name, floatingPointUiState.fromValue)
                        }.flowOn(Dispatchers.IO)
                            .collectAsStateWithLifecycle(initialValue = false)

                        ExtendedFloatingActionButton(
                            text = { Text(if (exists) "Show Steps" else "Generate Steps") },
                            icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.ic_ai),
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
            ScrollableTabRow(
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
                                text = tab.name,
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

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable { expanded = !expanded }
                        .focusable(false)
                        .padding(vertical = 4.dp),
                    value = when(currentTab) {
                        BaseN -> basesUiState.convertFrom.text
                        Complement -> complementUiState.convertFrom.text
                        Excess -> excessUiState.convertFrom.text
                        FloatingPoint -> floatingPointUiState.convertFrom.text
                    },
                    onValueChange = {},
                    textStyle = TextStyle(
                        lineBreak = LineBreak.Simple,
                        hyphens = Hyphens.Auto,
                        fontSize = 20.sp,
                        fontFamily = comicNeueFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    label = { Text(text = "Convert from_") },
                    readOnly = true,
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    trailingIcon = { TrailingIcon(expanded = expanded) },
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    when(currentTab) {
                        BaseN -> ConvertFrom.baseN.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.text) },
                                onClick = { basesViewModel.setConvertFrom(it); expanded = false }
                            )
                        }
                        Complement -> ConvertFrom.complementNotation.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.text) },
                                onClick = { complementViewModel.setConvertFrom(it); expanded = false }
                            )
                        }
                        Excess -> ConvertFrom.excessNotation.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.text) },
                                onClick = { excessViewModel.setConvertFrom(it); expanded = false }
                            )
                        }
                        FloatingPoint -> ConvertFrom.floatingPointNotation.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.text) },
                                onClick = { floatingPointViewModel.setConvertFrom(it); expanded = false }
                            )
                        }
                    }
                }
            }
            HorizontalDivider()

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