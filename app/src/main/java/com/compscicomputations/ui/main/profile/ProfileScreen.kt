package com.compscicomputations.ui.main.profile

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditOff
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.compscicomputations.theme.hintAdminPin
import com.compscicomputations.theme.hintCourse
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintNames
import com.compscicomputations.theme.hintPhone
import com.compscicomputations.theme.hintSchool
import com.compscicomputations.theme.hintUniversity
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.ui.OptionButton
import com.compscicomputations.ui.utils.isLoading
import com.compscicomputations.ui.utils.ui.shimmerBackground
import com.compscicomputations.utils.createImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navigateUp: () -> Unit,
    navigateAuth: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var logoutAlertDialog by remember { mutableStateOf(false) }
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
        isRefreshing = uiState.progressState.isLoading,
        navigateUp = navigateUp,
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
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Save") },
                        icon = { Icon(imageVector = Icons.Default.Save, contentDescription = "Save") },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        onClick = { viewModel.save() }
                    )
                }
            )
        },
    ) { contentPadding ->

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
                    if (uiState.imageBitmap != null && uiState.imageUri != null) {
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
                            .data(if (uiState.imageUri != null) uiState.imageUri else uiState.imageBitmap)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.img_profile),
                        error = painterResource(R.drawable.img_profile),
                        contentDescription = "Profile",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .shimmerBackground(showShimmer = uiState.progressState.isLoading)
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
                            .shimmerBackground(
                                showShimmer = uiState.progressState.isLoading,
                                RoundedCornerShape(4.dp)
                            )
                            .widthIn(min = 80.dp),
                        text = when {
                            uiState.isAdmin && uiState.isStudent -> "ADMIN | STUDENT"
                            uiState.isAdmin -> "ADMIN"
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
                            .widthIn(min = 128.dp)
                            .shimmerBackground(
                                uiState.progressState.isLoading,
                                RoundedCornerShape(4.dp)
                            ),
                        text = uiState.displayName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier
                            .widthIn(min = 180.dp)
                            .shimmerBackground(
                                uiState.progressState.isLoading,
                                RoundedCornerShape(4.dp)
                            ),
                        text = uiState.email,
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    uiState.phone?.let {
                        Text(
                            modifier = Modifier
                                .widthIn(min = 180.dp)
                                .shimmerBackground(
                                    uiState.progressState.isLoading,
                                    RoundedCornerShape(4.dp)
                                ),
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
                                .weight(.6f)
                                .shimmerBackground(
                                    uiState.progressState.isLoading,
                                    RoundedCornerShape(4.dp)
                                ),
                            text = uiState.university,
                            fontSize = 18.sp,
                            fontFamily = comicNeueFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "School:     ${uiState.school}",
                        fontSize = 18.sp,
                        fontFamily = comicNeueFamily
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Course:     ${uiState.course}",
                        fontSize = 18.sp,
                        fontFamily = comicNeueFamily
                    )
                }
            }
        }

        val (field1, field2, field3, field4, field5, field6, field7) = remember { FocusRequester.createRefs() }
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
                        value = uiState.displayName,
                        onValueChange = { viewModel.onDisplayNameChange(it);},
                        label = { Text(text = hintNames) },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        isError = uiState.displayNameError.isError,
                        supportingText = uiState.displayNameError.showMessage()
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field2)
                            .focusProperties { next = field3 }
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        label = { Text(text = hintEmail) },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        isError = uiState.emailError.isError,
                        supportingText = uiState.emailError.showMessage()
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field3)
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
                Column {
                    Text(
                        text = "My info",
                        modifier = Modifier.padding(start = 24.dp, end = 8.dp, top = 32.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicNeueFamily,
                        maxLines = 1
                    )
                    var dropdown by remember { mutableStateOf(false) }
                    val options = listOf("Student", "Admin", "Admin and student", "Not student nor admin")
                    ExposedDropdownMenuBox(
                        expanded = dropdown,
                        onExpandedChange = { dropdown = !dropdown }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            label = { Text(text = "I am _") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(top = 8.dp)
                                .clickable { dropdown = !dropdown }
                                .focusable(false),
                            value = when {
                                uiState.isAdmin && uiState.isStudent -> "Admin and student"
                                uiState.isAdmin -> "Admin"
                                uiState.isStudent -> "Student"
                                else -> "Not student nor admin"
                            },
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
                            options.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it) },
                                    onClick = {
                                        when(it) {
                                            options[0] -> viewModel.setIsStudentOrAdmin(
                                                isStudent = true,
                                                isAdmin = false
                                            )
                                            options[1] -> viewModel.setIsStudentOrAdmin(
                                                isStudent = false,
                                                isAdmin = true
                                            )
                                            options[2] -> viewModel.setIsStudentOrAdmin(
                                                isStudent = true,
                                                isAdmin = true
                                            )
                                            options[3] -> viewModel.setIsStudentOrAdmin(
                                                isStudent = false,
                                                isAdmin = false
                                            )
                                        }
                                        dropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item {
                if (uiState.isStudent) {
                    Column(Modifier.padding(top = 32.dp)) {
                        Text(
                            text = "Student info",
                            modifier = Modifier.padding(start = 24.dp, end = 8.dp, top = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = comicNeueFamily,
                            maxLines = 1
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(field4)
                                .focusProperties { next = field5 }
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.university,
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
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .focusRequester(field5)
                            .focusProperties { next = field6 }
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.school,
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
                            .focusRequester(field6)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.course,
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
            item {
                if (uiState.isAdmin) {
                    Column(Modifier.padding(top = 32.dp)) {
                        Text(
                            text = "Admin info",
                            modifier = Modifier.padding(start = 24.dp, end = 8.dp, top = 8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = comicNeueFamily,
                            maxLines = 1
                        )

                        var showPin by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            modifier = Modifier
                                .focusRequester(field7)
                                .onFocusChanged { if (!it.isFocused) showPin = false }
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.adminPin ?: "",
                            onValueChange = { viewModel.onAdminPinChange(it) },
                            label = { Text(text = hintAdminPin) },
                            singleLine = true,
                            visualTransformation =  if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPin = !showPin }) {
                                    Icon(if (showPin) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                                }
                            },
                            shape = RoundedCornerShape(22.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                            isError = uiState.adminPinError.isError,
                            supportingText = uiState.adminPinError.showMessage()
                        )
                    }
                }
            }
        }
    }
}