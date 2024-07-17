package com.compscicomputations.ui.auth.register

import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.core.client.auth.AuthDataStore.setTermsAccepted
import com.compscicomputations.core.client.auth.models.Usertype
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.theme.hintAdminCode
import com.compscicomputations.theme.hintPhone
import com.compscicomputations.theme.hintUsertype
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.CompSciAuthScaffold
import com.compscicomputations.ui.utils.ProgressState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteProfileScreen(
    viewModel: CompleteProfileViewModel,
    uiState: CompleteProfileUiState,
    navigateUp: () -> Unit,
    navigateTerms: () -> Unit,
    navigateMain: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(uiState.progressState) {
        if (uiState.progressState == ProgressState.Success) {
            navigateMain()
            Toast.makeText(context, "Welcome to CompSci Computations!", Toast.LENGTH_SHORT).show()
        }
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