package com.compscicomputations.ui.auth.register

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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.documentfile.provider.DocumentFile
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.compscicomputations.BuildConfig
import com.compscicomputations.R
import com.compscicomputations.ui.auth.FieldType
import com.compscicomputations.ui.auth.UserType
import com.compscicomputations.ui.auth.contain
import com.compscicomputations.ui.auth.getMessage
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.ui.theme.hintAdminCode
import com.compscicomputations.ui.theme.hintEmail
import com.compscicomputations.ui.theme.hintLastname
import com.compscicomputations.ui.theme.hintNames
import com.compscicomputations.ui.theme.hintPassword
import com.compscicomputations.ui.theme.hintPasswordConfirm
import com.compscicomputations.ui.theme.hintUserImage
import com.compscicomputations.ui.theme.hintUserType
import com.compscicomputations.utils.createImageFile

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun RegisterScreen(
    padding: PaddingValues = PaddingValues(8.dp),
    contentPadding: PaddingValues = PaddingValues(),
    uiState: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
    navigateUp: () -> Unit = {},
    navigateTerms: () -> Unit = {}
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.login_anim
        )
    )
    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        onEvent(RegisterUiEvent.OnImageUriChange(uri))
    }
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (!it) return@rememberLauncherForActivityResult
        else onEvent(RegisterUiEvent.OnImageUriChange(uri))
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) cameraLauncher.launch(uri)
        else Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = preloaderLottieComposition,
            progress = preloaderProgress,
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
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                }
            }

            Text(
                text = "Create an\nAccount",
                fontFamily = comicNeueFamily,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 52.sp,
                lineHeight = 52.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = contentPadding
            ) {
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
                            value = uiState.userType.name,
                            onValueChange = {},
                            label = {
                                Text(text = hintUserType)
                            },
                            readOnly = true,
                            shape = RoundedCornerShape(18.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userTypesExpanded) },
                            isError = uiState.errors contain FieldType.USER_TYPE,
                            supportingText = uiState.errors getMessage FieldType.USER_TYPE
                        )
                        ExposedDropdownMenu(
                            expanded = userTypesExpanded,
                            onDismissRequest = { userTypesExpanded = false }
                        ) {
                            UserType.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.name) },
                                    onClick = {
                                        onEvent(RegisterUiEvent.OnUserTypeChange(it))
                                        userTypesExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    AnimatedVisibility(uiState.userType == UserType.ADMIN) {
                        var showCode by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.adminCode ?: "",
                            onValueChange = { onEvent(RegisterUiEvent.OnAdminPinChange(it)) },
                            label = {
                                Text(text = hintAdminCode)
                            },
                            singleLine = true,
                            visualTransformation =  if (showCode) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showCode = !showCode }) {
                                    Icon(if (showCode) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "Admin Code Visibility")
                                }
                            },
                            shape = RoundedCornerShape(18.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            isError = uiState.errors contain FieldType.ADMIN_CODE,
                            supportingText = uiState.errors getMessage FieldType.ADMIN_CODE
                        )
                    }
                    /*if (uiState.userType == UserType.ADMIN) {
                        var showPin by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            value = uiState.adminPin,
                            onValueChange = { onEvent(RegisterUiEvent.OnAdminPinChange(it)) },
                            label = {
                                Text(text = hintAdminPin)
                            },
                            singleLine = true,
                            visualTransformation =  if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPin = !showPin }) {
                                    Icon(if (showPin) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                                }
                            },
                            shape = RoundedCornerShape(18.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            isError = uiState.errors contain FieldType.ADMIN_PIN,
                            supportingText = uiState.errors getMessage FieldType.ADMIN_PIN
                        )
                    }*/
                }
                item {
                    var imageExpanded by remember { mutableStateOf(false) }
                    val imageName = uiState.imageUri?.let { DocumentFile.fromSingleUri(LocalContext.current, it)?.name } ?: ""
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
                            label = {
                                Text(text = hintUserImage)
                            },
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(18.dp),
                            trailingIcon = {
                                IconButton(onClick = {}, modifier = Modifier.padding(end = 4.dp)) {
                                    GlideImage(
                                        model = uiState.imageUri,
                                        contentScale = ContentScale.FillBounds,
                                        loading = placeholder(R.drawable.img_profile),
                                        failure = placeholder(R.drawable.img_profile),
                                        transition = CrossFade,
                                        contentDescription = "Profile"
                                    )
                                }
                            },
                            isError = uiState.errors contain FieldType.IMAGE,
                            supportingText = uiState.errors getMessage FieldType.IMAGE
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
                        value = uiState.names,
                        onValueChange = { onEvent(RegisterUiEvent.OnNamesChange(it)) },
                        label = {
                            Text(text = hintNames)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = uiState.errors contain FieldType.NAMES,
                        supportingText = uiState.errors getMessage FieldType.NAMES
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.lastname,
                        onValueChange = { onEvent(RegisterUiEvent.OnLastnameChange(it)) },
                        label = {
                            Text(text = hintLastname)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = uiState.errors contain FieldType.LASTNAME,
                        supportingText = uiState.errors getMessage FieldType.LASTNAME
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.email,
                        onValueChange = { onEvent(RegisterUiEvent.OnEmailChange(it)) },
                        label = {
                            Text(text = hintEmail)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = uiState.errors contain FieldType.EMAIL,
                        supportingText = uiState.errors getMessage FieldType.EMAIL
                    )
                }
                item {
                    var showPassword by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.password,
                        onValueChange = { onEvent(RegisterUiEvent.OnPasswordChange(it)) },
                        label = {
                            Text(text = hintPassword)
                        },
                        singleLine = true,
                        visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = uiState.errors contain FieldType.PASSWORD,
                        supportingText = uiState.errors getMessage FieldType.PASSWORD
                    )
                }
                item {
                    var showPasswordConfirm by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        value = uiState.passwordConfirm,
                        onValueChange = { onEvent(RegisterUiEvent.OnPasswordConfirmChange(it)) },
                        label = {
                            Text(text = hintPasswordConfirm)
                        },
                        singleLine = true,
                        visualTransformation =  if (showPasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                                Icon(if (showPasswordConfirm) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = uiState.errors contain FieldType.PASSWORD_CONFIRM,
                        supportingText = uiState.errors getMessage FieldType.PASSWORD_CONFIRM
                    )
                }
                item {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = uiState.termsAccepted,
                            onCheckedChange = { onEvent(RegisterUiEvent.OnTermsAcceptChange(it)) }
                        )
                        Text(text = "I accept the", Modifier.padding(start = 10.dp), fontSize = 16.sp)
                        TextButton(onClick = navigateTerms, contentPadding = PaddingValues(horizontal = 3.dp)) {
                            Text(text = "Terms and Conditions.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    if (uiState.errors contain FieldType.TERMS) {
                        val error = uiState.errors.find { it.fieldType == FieldType.TERMS }
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                            text = error!!.errorMessage,
                            textAlign = TextAlign.Center,
                            color = OutlinedTextFieldDefaults.colors().errorLabelColor,
                            fontSize = 12.sp
                        )
                    }
                }
                item {
                    Button(onClick = { onEvent(RegisterUiEvent.Register) },
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

    }

}