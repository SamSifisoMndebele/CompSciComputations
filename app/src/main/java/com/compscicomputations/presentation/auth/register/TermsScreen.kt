package com.compscicomputations.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.ssmnd.pdf_viewer.PDFView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    navigateUp: () -> Unit,
    acceptTerms: (Boolean) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val darkTheme = isSystemInDarkTheme()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text(
                        "Terms and Conditions",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(imageVector = Icons.Outlined.ArrowBackIosNew, contentDescription = "back")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { acceptTerms(false) }) {
                        Icon(Icons.Outlined.Cancel, contentDescription = "Cancel", tint = Color.Red)
                    }
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { acceptTerms(true) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Check, "accept")
                        Text(text = " ACCEPT")
                    }
                }
            )
        }
    ) { innerPadding ->
        val context0 = LocalContext.current
        val inputBytes = try {
            context0.assets.open("docs/privacy_policy.pdf").readBytes()
        } catch (e: Exception) { null }
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            factory = { context ->
                val pdfView = PDFView(context, null)
                if (inputBytes != null) {
                    pdfView.fromBytes(inputBytes)
                        .enableDoubletap(true)
                        .nightMode(darkTheme)
                        .onError { err ->
                            Toast.makeText(context, err.message, Toast.LENGTH_SHORT).show()
                            navigateUp()
                        }
                        .load()
                } else {
                    Toast.makeText(context, "Error: File not found.", Toast.LENGTH_SHORT).show()
                    navigateUp()
                }
                pdfView
            }
        )
    }
}