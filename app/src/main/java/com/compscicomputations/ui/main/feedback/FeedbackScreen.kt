package com.compscicomputations.ui.main.feedback

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.compscicomputations.client.utils.Feedback
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isSuccess
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.ui.ExceptionDialog
import com.compscicomputations.ui.utils.ui.LoadingDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState.isSuccess) {
            Toast.makeText(context, "Feedback sent successfully!", Toast.LENGTH_SHORT).show()
        }
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.setPhotoUri(uri)
    }

    val (field1, field2, field3, field4) = remember { FocusRequester.createRefs() }
    CompSciScaffold(
        modifier = Modifier.fillMaxSize(),
        title = "Feedback",
        navigateUp = navigateUp,
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
        ) {
            item {
                var dropdown by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = dropdown,
                    onExpandedChange = { dropdown = !dropdown }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        label = { Text(text = "Subject") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                            .padding(top = 8.dp)
                            .clickable { dropdown = !dropdown }
                            .focusable(false),
                        value = uiState.subject.toString(),
                        onValueChange = {},
                        trailingIcon =  {
                            Icon(imageVector = if (dropdown) Icons.Filled.KeyboardArrowUp
                            else Icons.Filled.KeyboardArrowDown, contentDescription = null)
                        },
                        shape = RoundedCornerShape(22.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = dropdown,
                        onDismissRequest = { dropdown = false }
                    ) {
                        Subject.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.toString()) },
                                onClick = {
                                    viewModel.onSubjectChange(it)
                                    dropdown = false
                                }
                            )
                        }
                    }
                }
            }
            item {
                if (uiState.subject == Subject.Other) {
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field1)
                            .focusProperties { next = field2 }
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.otherSubject ?: "",
                        onValueChange = { viewModel.onOtherSubjectChange(it.takeIf { it.isNotBlank() }) },
                        label = { Text(text = "Other subject *") },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.subjectError.isError,
                        supportingText = uiState.subjectError.showMessage()
                    )
                }
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(field2)
                        .focusProperties { next = field3 }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = uiState.message,
                    onValueChange = { viewModel.onMessageChange(it) },
                    label = { Text(text = "Your ${uiState.subject} *") },
                    minLines = 2,
                    shape = RoundedCornerShape(22.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    isError = uiState.messageError.isError,
                    supportingText = uiState.messageError.showMessage()
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(field3)
                        .focusProperties { next = field4 }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = uiState.suggestion,
                    onValueChange = { viewModel.onSuggestionChange(it) },
                    label = { Text(text = "Suggestion for improvement") },
                    minLines = 2,
                    shape = RoundedCornerShape(22.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    )
                )
            }
            item {
                var imageExpanded by remember { mutableStateOf(false) }
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(22.dp),
                    onClick = {
                        if (uiState.imageUri != null) imageExpanded = !imageExpanded
                        else photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    DropdownMenu(
                        expanded = imageExpanded,
                        onDismissRequest = { imageExpanded = false },
                        offset = DpOffset(0.dp, (-120).dp),
                    ) {
                        DropdownMenuItem(
                            text = { Text("Select new image.") },
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                                imageExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Remove image") },
                            onClick = {
                                viewModel.setPhotoUri(null)
                                imageExpanded = false
                            }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp),
                            text = "Feedback Image",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = "Attach an image/screenshot to your feedback if you have any.",
                        )
                    }
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        model = uiState.imageUri,
                        contentDescription = "Feedback Image",
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
            item {
                Column {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = uiState.asAnonymous,
                            onCheckedChange = {
                                viewModel.setAsAnonymous(it)
                            }
                        )
                        Text(text = "Send as anonymous", Modifier.padding(start = 10.dp), fontSize = 16.sp)
                    }
                    if (uiState.asAnonymous) {
                        Text(
                            text = "Note that you will not get any response from us if you anonymously send feedback.",
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            fontSize = 13.sp,
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = { viewModel.onSendFeedback() },
                    enabled = uiState.isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(vertical = 18.dp)
                ) {
                    Text(
                        text = "Submit ${uiState.subject}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        fontFamily = comicNeueFamily
                    )
                }
            }
        }
    }

    LoadingDialog(
        message = when (uiState.progressState) {
            is ProgressState.Loading -> (uiState.progressState as ProgressState.Loading).message
            else -> ""
        },
        visible = uiState.progressState is ProgressState.Loading,
        onCancel = { viewModel.cancelSendFeedback() }
    ) {
        viewModel.onProgressStateChange(ProgressState.Idle)
    }

    ExceptionDialog(
        message = when (uiState.progressState) {
            is ProgressState.Error -> (uiState.progressState as ProgressState.Error).message ?: "Something went wrong."
            else -> ""
        },
        visible = uiState.progressState is ProgressState.Error
    ) {
        viewModel.onProgressStateChange(ProgressState.Idle)
    }

}