package com.compscicomputations.ui.auth.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.R
import com.compscicomputations.theme.AppRed
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.CompSciAuthScaffold
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.asActivity


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateOnboarding: () -> Unit,
    navigatePasswordLogin: () -> Unit,
    navigateMain: () -> Unit
) {
    val context = LocalContext.current
    val progressState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(progressState) {
        if (progressState == ProgressState.Success) {
            navigateMain()
            Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT).show()
        }
    }
    CompSciAuthScaffold(
        title = "Login",
        description = "Authenticate with Password or Google",
        navigateUp = null,
        navigateOnboarding = navigateOnboarding,
        progressState = progressState,
        onLoadingDismiss = { viewModel.cancelLogin() },
        onExceptionDismiss = { viewModel.onProgressStateChange(ProgressState.Idle) }
    ) {
        val activity by lazy { context.asActivity }

        OutlinedButton(
            onClick = { viewModel.onLoginPassword(activity, navigatePasswordLogin) },
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(64.dp),
                imageVector = Icons.Default.Password,
                contentDescription = "Password Button Icon",
                tint = AppRed
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 68.dp),
                text = "Continue Password",
                color = AppRed,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = comicNeueFamily,
                textAlign = TextAlign.Center
            )
        }

        OutlinedButton(
            onClick = { viewModel.onLoginWithGoogle(activity) },
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(64.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Button Icon"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 68.dp),
                text = "Continue with Google",
                color = AppRed,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = comicNeueFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}
