package com.compscicomputations.number_systems.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.SecureFlagPolicy
import com.compscicomputations.number_systems.R
import com.compscicomputations.number_systems.ui.bases.BasesScreen
import com.compscicomputations.number_systems.ui.complement.ComplementScreen
import com.compscicomputations.number_systems.ui.excess.ExcessScreen
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointScreen
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ui.AnnotatedText
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberSystems(
    navigateUp: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val titles = listOf("Base N", "Excess", "Complement", "Floating Point")
    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Number Systems",
        navigateUp = navigateUp,
        tabsBar = {
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedItem,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                indicator = { tabPositions ->
                    if (selectedItem < tabPositions.size) {
                        val width by animateDpAsState(
                            targetValue = tabPositions[selectedItem].contentWidth,
                            label = "TabAnimation"
                        )
                        TabRowDefaults.PrimaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedItem]),
                            width = width,
                            height = 6.dp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                },
                divider = {},
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.clip(RoundedCornerShape(22.dp)),
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                fontSize = 14.sp,
                                fontFamily = comicNeueFamily,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show Steps") },
                icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { showBottomSheet = true }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            when(selectedItem){
                0 -> BasesScreen()
                1 -> ExcessScreen()
                2 -> ComplementScreen()
                3 -> FloatingPointScreen()
            }

        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                properties = ModalBottomSheetProperties(
                    securePolicy = SecureFlagPolicy.Inherit,
                    isFocusable = true,
                    shouldDismissOnBackPress = false,
                )
            ) {
                val scrollState = rememberScrollState()
                AnnotatedText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState),
                    text = """
Let's break down the conversions:
                                                                                
**1. Decimal to Binary**

* **Repeated Division by 2:**
    * 259 / 2 = 129 remainder 1
    * 129 / 2 = 64 remainder 1
    * 64 / 2 = 32 remainder 0
    * 32 / 2 = 16 remainder 0
    * 16 / 2 = 8 remainder 0
    * 8 / 2 = 4 remainder 0
    * 4 / 2 = 2 remainder 0
    * 2 / 2 = 1 remainder 0
    * 1 / 2 = 0 remainder 1 
* **Read Remainders Upwards:** The binary representation is the remainders read from bottom to top: **100000011**

**2. Decimal to Octal**

* **Repeated Division by 8:**
    * 259 / 8 = 32 remainder 3
    * 32 / 8 = 4 remainder 0
    * 4 / 8 = 0 remainder 4
* **Read Remainders Upwards:** The octal representation is the remainders read from bottom to top: **403**

**3. Decimal to Hexadecimal**

* **Repeated Division by 16:**
    * 259 / 16 = 16 remainder 3
    * 16 / 16 = 1 remainder 0
* **Convert Remainders to Hexadecimal:**
    * 16 in hexadecimal is "10"
    * 3 in hexadecimal is "3"
* **Combine:** The hexadecimal representation is: **103**

**4. Unicode Character 'ă' to UTF-16**

* **Find Unicode Value:** The Unicode value for 'ă' is U+0103.
* **UTF-16 Encoding:** UTF-16 uses 2 bytes (16 bits) for characters in the Basic Multilingual Plane (BMP), which includes 'ă'. The encoding is straightforward:
    *  The Unicode value (0103) is converted to its binary representation: **00000000 00000100 00000000 00000011**
    *  This 16-bit value is directly represented in UTF-16: **00000000 00000100 00000000 00000011**

**Note:** UTF-16 has a "surrogate pair" mechanism for characters outside the BMP, but we don't need it here.
                    """.trimIndent()
                )
//                Box {
//
//                    IconButton(
//                        onClick = {
//                            scope.launch { sheetState.hide() }.invokeOnCompletion {
//                                if (!sheetState.isVisible) {
//                                    showBottomSheet = false
//                                }
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onSecondaryContainer
//                        )
//                    }
//                }
            }
        }
    }
}