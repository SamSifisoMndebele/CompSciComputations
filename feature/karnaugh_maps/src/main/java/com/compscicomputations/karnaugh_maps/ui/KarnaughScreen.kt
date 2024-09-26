package com.compscicomputations.karnaugh_maps.ui

import android.annotation.SuppressLint
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.R
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom.Expression
import com.compscicomputations.karnaugh_maps.data.model.CurrentTab
import com.compscicomputations.karnaugh_maps.data.model.CurrentTab.FourVars
import com.compscicomputations.karnaugh_maps.data.model.CurrentTab.ThreeVars
import com.compscicomputations.karnaugh_maps.data.model.CurrentTab.TwoVars
import com.compscicomputations.karnaugh_maps.data.source.local.datastore.FourVarsDataStore
import com.compscicomputations.karnaugh_maps.data.source.local.datastore.ThreeVarsDataStore
import com.compscicomputations.karnaugh_maps.data.source.local.datastore.TwoVarsDataStore
import com.compscicomputations.karnaugh_maps.data.source.local.datastore.setLastState
import com.compscicomputations.karnaugh_maps.ui.karnaugh4.Karnaugh4Screen
import com.compscicomputations.karnaugh_maps.ui.karnaugh4.Karnaugh4ViewModel
import com.compscicomputations.keyboard.KarnaughKeyboard
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.main.dynamic_feature.AIState.Idle
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.ui.StepsBox
import com.compscicomputations.ui.utils.ui.StepsList
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@SuppressLint("ClickableViewAccessibility")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun KarnaughScreen(
    navigateUp: () -> Unit,
    karnaugh2ViewModel: Karnaugh4ViewModel,
    karnaugh3ViewModel: Karnaugh4ViewModel,
    karnaugh4ViewModel: Karnaugh4ViewModel,
) {
    var currentTab by rememberSaveable { mutableStateOf(FourVars) }

    val karnaugh2UiState by karnaugh2ViewModel.uiState.collectAsStateWithLifecycle()
    val karnaugh3UiState by karnaugh3ViewModel.uiState.collectAsStateWithLifecycle()
    val karnaugh4UiState by karnaugh4ViewModel.uiState.collectAsStateWithLifecycle()

    val connectivityState by rememberConnectivityState()
    val isOnlineAndValidState = connectivityState.isAvailable && when(currentTab) {
        TwoVars -> karnaugh2ViewModel.textFieldState.value.text.isNotBlank() && karnaugh2UiState.convertFrom.isExpression
        ThreeVars -> karnaugh3ViewModel.textFieldState.value.text.isNotBlank() && karnaugh3UiState.convertFrom.isExpression
        FourVars -> karnaugh4ViewModel.textFieldState.value.text.isNotBlank() && karnaugh4UiState.convertFrom.isExpression
    }

    val focus = remember { FocusRequester() }
    val textFieldFocus = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showList by rememberSaveable { mutableStateOf(false) }

    CompSciScaffold(
        modifier = Modifier.fillMaxWidth(),
        title = "Karnaugh Maps",
        navigateUp = navigateUp,
        tabsBar = {
            if (connectivityState is ConnectionState.Unavailable) {
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
            if (textFieldFocus.value){
                KarnaughKeyboard(
                    onClose = { focus.requestFocus(); textFieldFocus.value = false },
                    textFieldState = when(currentTab) {
                        TwoVars -> karnaugh2ViewModel.textFieldState
                        ThreeVars -> karnaugh3ViewModel.textFieldState
                        FourVars -> karnaugh4ViewModel.textFieldState
                    }
                ) {

                }
            } else {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = {
                            when(currentTab) {
                                TwoVars -> karnaugh2ViewModel.textFieldState.value = TextFieldValue()
                                ThreeVars -> karnaugh3ViewModel.textFieldState.value = TextFieldValue()
                                FourVars -> karnaugh4ViewModel.textFieldState.value = TextFieldValue()
                            }
                            when(currentTab) {
                                TwoVars -> {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        TwoVarsDataStore.setLastState(context, karnaugh2UiState.convertFrom, "")
                                    }
                                    karnaugh2ViewModel.clear()
                                }
                                ThreeVars -> {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        ThreeVarsDataStore.setLastState(context, karnaugh3UiState.convertFrom, "")
                                    }
                                    karnaugh3ViewModel.clear()
                                }
                                FourVars -> {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        FourVarsDataStore.setLastState(context, karnaugh4UiState.convertFrom, "")
                                    }
                                    karnaugh4ViewModel.clear()
                                }
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
                                TwoVars -> karnaugh2ViewModel.aiResponseDao.exists(MODULE_NAME, TwoVars.name, Expression.name, karnaugh2ViewModel.textFieldState.value.text)
                                ThreeVars -> karnaugh3ViewModel.aiResponseDao.exists(MODULE_NAME, ThreeVars.name, Expression.name, karnaugh3ViewModel.textFieldState.value.text)
                                FourVars -> karnaugh4ViewModel.aiResponseDao.exists(MODULE_NAME, FourVars.name, Expression.name, karnaugh4ViewModel.textFieldState.value.text)
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
                                        TwoVars -> karnaugh2ViewModel.generateSteps()
                                        ThreeVars -> karnaugh3ViewModel.generateSteps()
                                        FourVars -> karnaugh4ViewModel.generateSteps()
                                    }
                                }
                            )
                        }
                    } else null
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
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
            Spacer(modifier = Modifier.height(8.dp))

            when(currentTab) {
                TwoVars -> Text("TwoVars")
                ThreeVars -> Text("ThreeVars")
                FourVars -> Karnaugh4Screen(
                    modifier = Modifier.focusRequester(focus),
                    viewModel = karnaugh4ViewModel,
                    uiState = karnaugh4UiState,
                    textFieldState = when(currentTab) {
                        TwoVars -> karnaugh2ViewModel.textFieldState
                        ThreeVars -> karnaugh3ViewModel.textFieldState
                        FourVars -> karnaugh4ViewModel.textFieldState
                    },
                    textFieldFocus = textFieldFocus
                )
            }
        }

        when(currentTab) {
            TwoVars -> StepsBox(
                scope = coroutineScope,
                aiState = karnaugh2UiState.aiState,
                onDismissRequest = { karnaugh2ViewModel.setAiState(Idle) },
                onCancel = { karnaugh2ViewModel.cancelJob() },
                onRegenerate = { karnaugh2ViewModel.regenerateSteps() },
            )
            ThreeVars -> StepsBox(
                scope = coroutineScope,
                aiState = karnaugh3UiState.aiState,
                onDismissRequest = { karnaugh3ViewModel.setAiState(Idle) },
                onCancel = { karnaugh3ViewModel.cancelJob() },
                onRegenerate = { karnaugh3ViewModel.regenerateSteps() },
            )
            FourVars -> StepsBox(
                scope = coroutineScope,
                aiState = karnaugh4UiState.aiState,
                onDismissRequest = { karnaugh4ViewModel.setAiState(Idle) },
                onCancel = { karnaugh4ViewModel.cancelJob() },
                onRegenerate = { karnaugh4ViewModel.regenerateSteps() },
            )
        }

        when(currentTab) {
            TwoVars -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = karnaugh2ViewModel.responsesFlow,
                onDelete = { karnaugh2ViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    karnaugh2ViewModel.onExpressionChange(TextFieldValue(it.value, TextRange(it.value.length)))
                    coroutineScope.launch(Dispatchers.IO) {
                        context.setLastState(TwoVars, it.convertFrom, it.value)
                    }
                    karnaugh2ViewModel.generateSteps()
                }
            )
            ThreeVars -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = karnaugh3ViewModel.responsesFlow,
                onDelete = { karnaugh3ViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    karnaugh3ViewModel.onExpressionChange(TextFieldValue(it.value, TextRange(it.value.length)))
                    coroutineScope.launch(Dispatchers.IO) {
                        context.setLastState(ThreeVars, it.convertFrom, it.value)
                    }
                    karnaugh3ViewModel.generateSteps()
                }
            )
            FourVars -> StepsList(
                show = showList,
                onDismissRequest = { showList = false },
                listFlow = karnaugh4ViewModel.responsesFlow,
                onDelete = { karnaugh4ViewModel.deleteResponse(it) },
                onSelect = {
                    showList = false
                    karnaugh4ViewModel.onExpressionChange(TextFieldValue(it.value, TextRange(it.value.length)))
                    coroutineScope.launch(Dispatchers.IO) {
                        context.setLastState(FourVars, it.convertFrom, it.value)
                    }
                    karnaugh4ViewModel.generateSteps()
                }
            )
        }
    }


}