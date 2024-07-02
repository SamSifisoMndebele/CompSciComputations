package com.compscicomputations.presentation.auth.register

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.documentfile.provider.DocumentFile
import coil.compose.AsyncImage
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.compscicomputations.BuildConfig
import com.compscicomputations.R
import com.compscicomputations.core.ktor_client.model.Usertype
import com.compscicomputations.presentation.LoadingDialog
import com.compscicomputations.presentation.auth.AuthDataStore
import com.compscicomputations.presentation.auth.isError
import com.compscicomputations.presentation.auth.showMessage
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.ui.theme.hintAdminCode
import com.compscicomputations.ui.theme.hintDisplayName
import com.compscicomputations.ui.theme.hintEmail
import com.compscicomputations.ui.theme.hintPassword
import com.compscicomputations.ui.theme.hintPasswordConfirm
import com.compscicomputations.ui.theme.hintPhone
import com.compscicomputations.ui.theme.hintUserImage
import com.compscicomputations.ui.theme.hintUserType
import com.compscicomputations.utils.createImageFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    padding: PaddingValues = PaddingValues(8.dp),
    lottieComposition: LottieComposition?,
    lottieProgress: Float,
    viewModel: RegisterViewModel,
    navigateUp: () -> Unit,
    navigateMain: () -> Unit,
    navigateTerms: () -> Unit
) {
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.setPhotoUri(uri)
    }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (!it) return@rememberLauncherForActivityResult
        viewModel.setPhotoUri(uri)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) cameraLauncher.launch(uri)
        else Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }
    val showProgress by viewModel.showProgress.collectAsState()
    val authDataStore = AuthDataStore(context)

    val userType by viewModel.userType.collectAsState()
    val adminCode by viewModel.adminCode.collectAsState()
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordConfirm by viewModel.passwordConfirm.collectAsState()
    val photoUri by viewModel.photoUri.collectAsState()
    val termsAccepted by viewModel.termsAccepted.collectAsState()
    authDataStore.SetTermsAccepted(termsAccepted)
    val userRegistered by viewModel.userRegistered.collectAsState()
    LaunchedEffect(userRegistered) {
        if (userRegistered) {
            navigateMain()
            Toast.makeText(context, "$displayName, created an account successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = lottieProgress,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .alpha(.5f),
            contentScale = ContentScale.FillWidth
        )

        Column(
            Modifier.padding(padding)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = navigateUp, Modifier.padding(vertical = 4.dp)) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Image(
                    painter = painterResource(id = R.drawable.img_logo_name),
                    contentDescription = stringResource(id = R.string.app_name),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(128.dp)
                        .padding(start = 8.dp),
                )
//                Spacer(modifier = Modifier.weight(1f))
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
//                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
//                contentPadding = contentPadding
            ) {
                item {
                    Text(
                        text = "Create an\nAccount",
                        fontFamily = comicNeueFamily,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 52.sp,
                        lineHeight = 52.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                item {
                    var userTypesExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = userTypesExpanded,
                        onExpandedChange = { userTypesExpanded = !userTypesExpanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .clickable { userTypesExpanded = !userTypesExpanded }
                                .focusable(false)
                                .padding(vertical = 4.dp),
                            value = userType.sqlName,
                            onValueChange = {},
                            label = { Text(text = hintUserType) },
                            readOnly = true,
                            shape = RoundedCornerShape(18.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userTypesExpanded) },
                        )
                        ExposedDropdownMenu(
                            expanded = userTypesExpanded,
                            onDismissRequest = { userTypesExpanded = false }
                        ) {
                            Usertype.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.sqlName) },
                                    onClick = {
                                        viewModel.setUserType(it)
                                        userTypesExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    AnimatedVisibility(userType == Usertype.ADMIN) {
                        var showCode by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = adminCode,
                            onValueChange = { viewModel.setAdminCode(it); viewModel.adminCodeError = null },
                            label = { Text(text = hintAdminCode) },
                            singleLine = true,
                            visualTransformation =  if (showCode) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showCode = !showCode }) {
                                    Icon(if (showCode) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "Admin Code Visibility")
                                }
                            },
                            shape = RoundedCornerShape(18.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            isError = viewModel.adminCodeError.isError,
                            supportingText = viewModel.adminCodeError.showMessage()
                        )
                    }
                }
                item {
                    var imageExpanded by remember { mutableStateOf(false) }
                    val imageName = photoUri?.let { DocumentFile.fromSingleUri(LocalContext.current, it)?.name } ?: ""
                    ExposedDropdownMenuBox(
                        expanded = imageExpanded,
                        onExpandedChange = { imageExpanded = !imageExpanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .focusable(false)
                                .padding(vertical = 4.dp),
                            value = imageName,
                            onValueChange = {},
                            label = { Text(text = hintUserImage) },
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(18.dp),
                            trailingIcon = {
                                IconButton(onClick = {}, modifier = Modifier.padding(end = 4.dp)) {
                                    AsyncImage(
                                        model = photoUri,
                                        contentDescription = "Profile",
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                            },
                            isError = viewModel.photoUriError.isError,
                            supportingText = viewModel.photoUriError.showMessage()
                        )
                        ExposedDropdownMenu(
                            expanded = imageExpanded,
                            onDismissRequest = { imageExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Select your image.") },
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                    imageExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Take a new picture.") },
                                onClick = {
                                    val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                    if (permissionCheckResult == PERMISSION_GRANTED) cameraLauncher.launch(uri)
                                    else permissionLauncher.launch(Manifest.permission.CAMERA)
                                    imageExpanded = false
                                }
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = displayName,
                        onValueChange = { viewModel.setDisplayName(it); viewModel.displayNameError = null },
                        label = { Text(text = hintDisplayName) },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = viewModel.displayNameError.isError,
                        supportingText = viewModel.displayNameError.showMessage()
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = email,
                        onValueChange = { viewModel.setEmail(it); viewModel.emailError = null },
                        label = { Text(text = hintEmail) },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = viewModel.emailError.isError,
                        supportingText = viewModel.emailError.showMessage()
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = phone,
                        onValueChange = { viewModel.setPhone(it); viewModel.phoneError = null },
                        label = { Text(text = hintPhone) },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = viewModel.phoneError.isError,
                        supportingText = viewModel.phoneError.showMessage()
                    )
                }
                item {
                    var showPassword by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = password,
                        onValueChange = { viewModel.setPassword(it); viewModel.passwordError = null },
                        label = { Text(text = hintPassword) },
                        singleLine = true,
                        visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = viewModel.passwordError.isError,
                        supportingText = viewModel.passwordError.showMessage()
                    )
                }
                item {
                    var showPasswordConfirm by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = passwordConfirm,
                        onValueChange = { viewModel.setPasswordConfirm(it); viewModel.passwordConfirmError = null },
                        label = { Text(text = hintPasswordConfirm) },
                        singleLine = true,
                        visualTransformation = if (showPasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                                Icon(if (showPasswordConfirm) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = viewModel.passwordConfirmError.isError,
                        supportingText = viewModel.passwordConfirmError.showMessage()
                    )
                }
                item {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = termsAccepted,
                            onCheckedChange = { viewModel.setTermsAccepted(it) }
                        )
                        Text(text = "I accept the", Modifier.padding(start = 10.dp), fontSize = 16.sp)
                        TextButton(onClick = navigateTerms, contentPadding = PaddingValues(horizontal = 3.dp)) {
                            Text(text = "Terms and Conditions.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
                item {
                    SnackbarHost(hostState = viewModel.snackBarHostState,
                        modifier = Modifier
                            .fillMaxWidth()) {
                        Snackbar(
                            snackbarData = it,
                            containerColor = OutlinedTextFieldDefaults.colors().errorLabelColor,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                item {
                    Button(onClick = { viewModel.onRegister() },
                        enabled = displayName.isNotBlank() && email.isNotBlank() &&
                                password.isNotBlank() && passwordConfirm.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(vertical = 18.dp)
                    ) {
                        Text(text = "REGISTER", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                            fontFamily = comicNeueFamily)
                    }
                }
            }
        }

        LoadingDialog(show = showProgress, message = "Creating an account...")
    }

}