package com.compscicomputations.ui.auth.register

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.compscicomputations.BuildConfig
import com.compscicomputations.client.auth.data.source.local.AuthDataStore.setTermsAccepted
import com.compscicomputations.theme.AppRed
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintLastname
import com.compscicomputations.theme.hintNames
import com.compscicomputations.theme.hintPassword
import com.compscicomputations.theme.hintPasswordConfirm
import com.compscicomputations.theme.hintProfileImage
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ui.CompSciAuthScaffold
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isSuccess
import com.compscicomputations.ui.utils.ui.shimmerBackground
import com.compscicomputations.utils.asActivity
import com.compscicomputations.utils.createImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateUp: () -> Unit,
    navigateTerms: () -> Unit,
    navigateOnboarding: () -> Unit,
    navigateMain: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState.isSuccess) {
            navigateMain()
            Toast.makeText(context, "Registered in successfully!", Toast.LENGTH_SHORT).show()
        }
    }
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

    val (field1, field2, field3, field4, field5) = remember { FocusRequester.createRefs() }
    CompSciAuthScaffold(
        title = "Register",
        description = "Register your account using your email and password.",
        progressState = uiState.progressState,
        onLoadingDismiss = { viewModel.cancelRegister() },
        onExceptionDismiss = { viewModel.onProgressStateChange(ProgressState.Idle) },
        navigateUp = navigateUp,
        navigateOnboarding = navigateOnboarding
    ) {
        var imageExpanded by remember { mutableStateOf(false) }
        val imageName = uiState.imageUri?.let { DocumentFile.fromSingleUri(context, it)?.name }
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(22.dp),
            onClick = { imageExpanded = !imageExpanded }
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
                    text = { Text("Take a new picture.") },
                    onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PERMISSION_GRANTED) cameraLauncher.launch(uri)
                        else permissionLauncher.launch(Manifest.permission.CAMERA)
                        imageExpanded = false
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        text = hintProfileImage,
                        color = AppRed,
                        fontSize = 18.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .weight(1f),
                        text = imageName.takeIf { !it.isNullOrBlank() } ?: "Select your image or take a new picture",
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .shimmerBackground(uiState.imageUri == null)
                        .size(120.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    model = uiState.imageUri,
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                )
            }
        }

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            isError = uiState.namesError.isError,
            supportingText = uiState.namesError.showMessage()
        )

        OutlinedTextField(
            modifier = Modifier
                .focusRequester(field2)
                .focusProperties { next = field3 }
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.lastname,
            onValueChange = { viewModel.onLastnameChange(it);},
            label = { Text(text = hintLastname) },
            singleLine = true,
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            isError = uiState.lastnameError.isError,
            supportingText = uiState.lastnameError.showMessage()
        )

        OutlinedTextField(
            modifier = Modifier
                .focusRequester(field3)
                .focusProperties { next = field4 }
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

        var showPassword by remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(field4)
                .focusProperties { next = field5 }
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it)},
            label = { Text(text = hintPassword) },
            singleLine = true,
            visualTransformation =  if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                }
            },
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            isError = uiState.passwordError.isError,
            supportingText = uiState.passwordError.showMessage()
        )

        var showPasswordConfirm by remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(field5)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.passwordConfirm,
            onValueChange = { viewModel.onPasswordConfirmChange(it) },
            label = { Text(text = hintPasswordConfirm) },
            singleLine = true,
            visualTransformation = if (showPasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                    Icon(if (showPasswordConfirm) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "hide_password")
                }
            },
            shape = RoundedCornerShape(22.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.passwordConfirmError.isError,
            supportingText = uiState.passwordConfirmError.showMessage()
        )

        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = uiState.termsAccepted,
                onCheckedChange = {
                    coroutineScope.launch(Dispatchers.IO) { context.setTermsAccepted(it) }
                }
            )
            Text(text = "I accept the", Modifier.padding(start = 10.dp), fontSize = 16.sp)
            TextButton(onClick = navigateTerms, contentPadding = PaddingValues(horizontal = 3.dp)) {
                Text(text = "Terms and Conditions.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            uiState.termsAcceptedError.showMessage()
        }

        val activity by lazy { context.asActivity }
        val credentialManager by lazy { CredentialManager.create(activity) }
        Button(
            onClick = {
                viewModel.onRegister(context) { email, password ->
                    val createPasswordRequest = CreatePasswordRequest(email, password)
                    credentialManager.createCredential(activity, createPasswordRequest)
                }
            },
            enabled = uiState.isValid,
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