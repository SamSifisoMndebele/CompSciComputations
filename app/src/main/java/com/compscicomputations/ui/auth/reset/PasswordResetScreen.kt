package com.compscicomputations.ui.auth.reset

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.compscicomputations.R
import com.compscicomputations.ui.LoadingDialog
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.ui.theme.hintEmail

@Composable
fun PasswordResetScreen(
    padding: PaddingValues = PaddingValues(8.dp),
    viewModel: ResetViewModel,
    lottieComposition: LottieComposition?,
    lottieProgress: Float,
    navigateUp: () -> Unit
) {
    val emailSent by viewModel.emailSent.collectAsState()
    val context =  LocalContext.current
    LaunchedEffect(emailSent) {
        if (emailSent) {
            Toast.makeText(context, "Reset email is sent successfully!", Toast.LENGTH_SHORT).show()
            navigateUp()
        }
    }
    val showProgress by viewModel.showProgress.collectAsState()
    Box(contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = navigateUp, Modifier.padding(vertical = 4.dp)) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Image(
                    painter = painterResource(id =+ R.drawable.img_logo_name),
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
            Text(
                text = "Password\nReset",
                fontFamily = comicNeueFamily,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 52.sp,
                lineHeight = 52.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            Column(
                Modifier.padding(padding)
            ) {
                LottieAnimation(
                    composition = lottieComposition,
                    progress = lottieProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentScale = ContentScale.FillWidth
                )

                val email by viewModel.email.collectAsState()

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = {
                        Text(text = hintEmail)
                    },
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = viewModel.emailError.isError,
                    supportingText = viewModel.emailError.showMessage()
                )
                SnackbarHost(hostState = viewModel.snackBarHostState,
                    modifier = Modifier
                        .fillMaxWidth()) {
                    Snackbar(
                        snackbarData = it,
                        containerColor = OutlinedTextFieldDefaults.colors().errorLabelColor,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Button(
                    onClick = { viewModel.onSendEmail() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(text = "Sent Reset Email", fontWeight = FontWeight.Bold, fontSize = 22.sp,
                        fontFamily = comicNeueFamily)
                }
            }
        }
        LoadingDialog(show = showProgress)
    }
}