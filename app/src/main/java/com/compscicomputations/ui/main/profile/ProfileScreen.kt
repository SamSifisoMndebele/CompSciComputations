package com.compscicomputations.ui.main.profile

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.compscicomputations.R
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintCourse
import com.compscicomputations.theme.hintLastname
import com.compscicomputations.theme.hintNames
import com.compscicomputations.theme.hintPhone
import com.compscicomputations.theme.hintSchool
import com.compscicomputations.theme.hintUniversity
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isError
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.isLoading
import com.compscicomputations.ui.utils.ui.ExceptionDialog
import com.compscicomputations.ui.utils.ui.LoadingDialog
import com.compscicomputations.ui.utils.ui.shimmerBackground
import com.compscicomputations.utils.createImageFile

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateUp: () -> Unit,
    navigateAuth: () -> Unit
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val progressState by viewModel.progressState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var logoutAlertDialog by remember { mutableStateOf(false) }
    if (logoutAlertDialog) {
        AlertDialog(
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Alert Icon") },
            title = { Text(text = "LOGOUT", fontFamily = comicNeueFamily) },
            text = { Text(text = "Do you want to logout?", fontFamily = comicNeueFamily) },
            onDismissRequest = { logoutAlertDialog = false },
            confirmButton = {
                TextButton(onClick = { viewModel.logout() }) {
                    Text("Logout", fontFamily = comicNeueFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { logoutAlertDialog = false }) {
                    Text("Dismiss", fontFamily = comicNeueFamily)
                }
            }
        )
    }
    var navigateUpAlertDialog by remember { mutableStateOf(false) }
    BackHandler {
        if (viewModel.isNotChanged) navigateUp()
        else navigateUpAlertDialog = true
    }
    if (navigateUpAlertDialog) {
        AlertDialog(
            icon = { Icon(Icons.Outlined.Warning, contentDescription = "Alert Icon") },
            title = { Text(text = "Save changes!", fontFamily = comicNeueFamily) },
            text = { Text(text = "Save your changed information before exit?", fontFamily = comicNeueFamily) },
            onDismissRequest = { navigateUpAlertDialog = false },
            confirmButton = {
                TextButton(onClick = { navigateUpAlertDialog = false; viewModel.save { navigateUp() } }) {
                    Text("Save", fontFamily = comicNeueFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { navigateUpAlertDialog = false; navigateUp() }) {
                    Text("Discard", fontFamily = comicNeueFamily)
                }
            }
        )
    }
    LaunchedEffect(uiState.isSignedIn) {
        logoutAlertDialog = false
        if (!uiState.isSignedIn) navigateAuth()
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.setPhotoUri(uri)
    }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, "com.compscicomputations.provider", file)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (!it) return@rememberLauncherForActivityResult
        viewModel.setPhotoUri(uri)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) cameraLauncher.launch(uri)
        else Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }
    CompSciScaffold(
        title = "Profile",
        snackBarHost = { SnackbarHost(hostState = viewModel.snackBarHostState) },
        navigateUp = {
            if (viewModel.isNotChanged) navigateUp()
            else navigateUpAlertDialog = true
        },
        isRefreshing = progressState is ProgressState.Refreshing,
        onRefresh = { viewModel.onRefresh() },
        bottomBar = {
            BottomAppBar(
                actions = {
//                    IconButton(onClick = {
//                        // TODO: Delete profile
//                    }) {
//                        Icon(
//                            Icons.Outlined.Delete,
//                            contentDescription = "Delete profile",
//                            tint = Color.Red
//                        )
//                    }
                    Button(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.10f),
                        ),
                        onClick = { logoutAlertDialog = true },
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout Icon",
                            tint = Color.Red
                        )
                        Text(
                            text = "Logout",
                            modifier = Modifier.padding(7.dp),
                            fontSize = 20.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontFamily = comicNeueFamily,
                            maxLines = 1
                        )
                    }
                },
                floatingActionButton = {
                    if (!viewModel.isNotChanged) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = "Save",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = comicNeueFamily,
                                    maxLines = 1
                                )
                            },
                            icon = { Icon(imageVector = Icons.Default.Save, contentDescription = "Save") },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            onClick = { viewModel.save() }
                        )
                    }
                }
            )
        },
    ) { contentPadding ->

        var imageExpanded by remember { mutableStateOf(false) }

        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = 8.dp,
                    end = 8.dp
                )
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            )
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenu(
                    expanded = imageExpanded,
                    onDismissRequest = { imageExpanded = false },
                    offset = DpOffset(0.dp, (-120).dp),
                ) {
                    DropdownMenuItem(
                        text = { Text("Select your image.") },
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            imageExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Take a new image.") },
                        onClick = {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                            if (permissionCheckResult == PERMISSION_GRANTED) cameraLauncher.launch(uri)
                            else permissionLauncher.launch(Manifest.permission.CAMERA)
                            imageExpanded = false
                        }
                    )
                    if (userState?.imageBitmap != null && uiState.imageUri != null) {
                        DropdownMenuItem(
                            text = { Text("Restore image.") },
                            onClick = {
                                viewModel.setPhotoUri(null)
                                imageExpanded = false
                            }
                        )
                    }
                }

                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (uiState.imageUri != null) uiState.imageUri else userState?.imageBitmap)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.img_profile),
                        error = painterResource(R.drawable.img_profile),
                        contentDescription = "Profile",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .shimmerBackground(showShimmer = progressState.isLoading)
                            .size(180.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { imageExpanded = !imageExpanded },
                        onSuccess = {},
                        onError = {},
                        onLoading = {}
                    )
                    OutlinedButton(
                        onClick = { imageExpanded = !imageExpanded  },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Text(text = "Change Image")
                    }
                }

                Column(
                    Modifier.weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .widthIn(min = 80.dp),
                        text = when {
                            userState?.isAdmin == true && uiState.isStudent -> "ADMIN | STUDENT"
                            userState?.isAdmin == true -> "ADMIN"
                            uiState.isStudent -> "STUDENT"
                            else -> ""
                        },
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier
                            .widthIn(min = 128.dp),
                        text = uiState.displayName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier
                            .widthIn(min = 180.dp),
                        text = userState?.email ?: "",
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    uiState.phone?.let {
                        Text(
                            modifier = Modifier
                                .widthIn(min = 180.dp),
                            text = it,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
            if (uiState.isStudent) {
                HorizontalDivider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                    Text(
                        text = "STUDENT INFO",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(
                            modifier = Modifier.weight(.4f),
                            text = "University:",
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                        )
                        Text(
                            modifier = Modifier
                                .weight(.6f),
                            text = uiState.university ?: "_",
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            modifier = Modifier.weight(.4f),
                            text = "School:",
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                        )
                        Text(
                            modifier = Modifier
                                .weight(.6f),
                            text = uiState.school ?: "_",
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            modifier = Modifier.weight(.4f),
                            text = "Course:",
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                        )
                        Text(
                            modifier = Modifier
                                .weight(.6f),
                            text = uiState.course ?: "_",
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        val (field1, field2, field4, field5, field6, field7) = remember { FocusRequester.createRefs() }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = contentPadding.calculateBottomPadding() - 8.dp
                )
                .weight(1f),
        ) {
            item {
                Column {
                    Text(
                        text = "Profile info",
                        modifier = Modifier.padding(start = 24.dp, end = 8.dp, top = 32.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily,
                        maxLines = 1
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field1)
                            .focusProperties { next = field2 }
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.names,
                        onValueChange = { viewModel.onNamesChange(it);},
                        label = { Text(text = hintNames) },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.namesError.isError,
                        supportingText = uiState.namesError.showMessage()
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field2)
                            .focusProperties { next = field4 }
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.lastname,
                        onValueChange = { viewModel.onLastnameChange(it);},
                        label = { Text(text = hintLastname) },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.lastnameError.isError,
                        supportingText = uiState.lastnameError.showMessage()
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field4)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.phone ?: "",
                        onValueChange = { viewModel.onPhoneChange(it) },
                        label = { Text(text = hintPhone) },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                        isError = uiState.phoneError.isError,
                        supportingText = uiState.phoneError.showMessage()
                    )
                }
            }
            item {
                OutlinedButton(
                    onClick = { viewModel.setIsStudent(!uiState.isStudent) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "I am a Student",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily,
                        maxLines = 1,
                    )
                    Switch(
                        checked = uiState.isStudent,
                        onCheckedChange = { viewModel.setIsStudent(it) }
                    )
                }
            }
            item {
                if (uiState.isStudent) {
                    Column(Modifier.padding(bottom = 32.dp)) {
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(field5)
                                .focusProperties { next = field6 }
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.university ?: "",
                            onValueChange = { viewModel.onUniversityChange(it) },
                            label = { Text(text = hintUniversity) },
                            singleLine = true,
                            shape = RoundedCornerShape(22.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isError = uiState.universityError.isError,
                            supportingText = uiState.universityError.showMessage()
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(field6)
                                .focusProperties { next = field7 }
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.school ?: "",
                            onValueChange = { viewModel.onSchoolChange(it) },
                            label = { Text(text = hintSchool) },
                            singleLine = true,
                            shape = RoundedCornerShape(22.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            isError = uiState.schoolError.isError,
                            supportingText = uiState.schoolError.showMessage()
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(field7)
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.course ?: "",
                            onValueChange = { viewModel.onCourseChange(it) },
                            label = { Text(text = hintCourse) },
                            singleLine = true,
                            shape = RoundedCornerShape(22.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            isError = uiState.courseError.isError,
                            supportingText = uiState.courseError.showMessage()
                        )
                    }
                }
            }
        }
    }

    LoadingDialog(
        message = if (progressState is ProgressState.Loading) (progressState as ProgressState.Loading).message else "",
        visible = progressState.isLoading,
        onDismiss = {
            viewModel.setProgressState(ProgressState.Idle)
        },
        onCancel = {
            viewModel.cancelJob()
        }
    )
    ExceptionDialog(
        message = (if (progressState is ProgressState.Error) (progressState as ProgressState.Error).message else "")
            ?:"An unexpected error occurred.",
        visible = progressState.isError,
        onDismiss = {
            viewModel.setProgressState(ProgressState.Idle)
        }
    )
}