package com.compscicomputations.ui.auth.register

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.compscicomputations.BuildConfig
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintAdminCode
import com.compscicomputations.theme.hintDisplayName
import com.compscicomputations.theme.hintEmail
import com.compscicomputations.theme.hintPassword
import com.compscicomputations.theme.hintPasswordConfirm
import com.compscicomputations.theme.hintPhone
import com.compscicomputations.theme.hintUserImage
import com.compscicomputations.theme.hintUsertype
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.CompSciAuthScaffold
import com.compscicomputations.utils.createImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteProfileScreen(
    viewModel: CompleteProfileViewModel,
    uiState: CompleteProfileUiState,
    navigateUp: () -> Unit,
    navigateMain: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState == ProgressState.Success) {
            navigateMain()
            Toast.makeText(context, "Welcome to CompSci Computations!", Toast.LENGTH_SHORT).show()
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

    CompSciAuthScaffold(
        title = "Complete Profile",
        description = "Give us your additional required information for great experience.",
        progressState = uiState.progressState,
        onLoadingDismiss = { viewModel.cancelRegister() },
        onExceptionDismiss = { viewModel.onProgressStateChange(ProgressState.Idle) },
        navigateUp = navigateUp,
        navigateOnboarding = null
    ) {
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
                value = uiState.usertype.name,
                onValueChange = {},
                label = { Text(text = hintUsertype) },
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
                        text = { Text(text = it.name) },
                        onClick = {
                            viewModel.setUsertype(it)
                            userTypesExpanded = false
                        }
                    )
                }
            }
        }

        AnimatedVisibility(uiState.isAdmin) {
            var showCode by remember { mutableStateOf(false) }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = uiState.adminCode ?: "",
                onValueChange = { viewModel.onAdminCodeChange(it) },
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
                isError = uiState.adminCodeError.isError,
                supportingText = uiState.adminCodeError.showMessage()
            )
        }

        var imageExpanded by remember { mutableStateOf(false) }
        val imageName = uiState.photoUri?.let { DocumentFile.fromSingleUri(context, it)?.name } ?: ""
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
                            model = uiState.photoUri,
                            contentDescription = "Profile",
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.displayName,
            onValueChange = { viewModel.onDisplayNameChange(it);},
            label = { Text(text = hintDisplayName) },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = uiState.displayNameError.isError,
            supportingText = uiState.displayNameError.showMessage()
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.phone ?: "",
            onValueChange = { viewModel.onPhoneChange(it) },
            label = { Text(text = hintPhone) },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = uiState.phoneError.isError,
            supportingText = uiState.phoneError.showMessage()
        )

        Button(onClick = { viewModel.onRegister() },
            enabled = uiState.isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Text(text = "Complete Profile", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                fontFamily = comicNeueFamily)
        }
    }
}