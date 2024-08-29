package com.compscicomputations.ui.auth.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compscicomputations.R
import com.compscicomputations.client.auth.data.source.local.UserDataStore.Companion.isUserSignedIn
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.navigation.Auth
import com.compscicomputations.ui.navigation.Main
import com.compscicomputations.ui.navigation.Splash
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val connectivityState by rememberConnectivityState()
    LaunchedEffect(connectivityState) {
        if (context.isUserSignedIn()) navController.navigate(route = Main) {
            popUpTo<Splash> { inclusive = true }
            launchSingleTop = true
        }
        else {
            if (connectivityState.isAvailable) {
                navController.navigate(route = Auth) {
                    popUpTo<Splash> { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.img_logo_name),
                contentDescription = "App Logo",
                contentScale = ContentScale.FillWidth
            )
            if (connectivityState.isUnavailable) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        imageVector = Icons.Outlined.WifiOff,
                        contentDescription = "No connection",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Authentication may not be successful,\n" +
                                "please check your internet connection.",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontFamily = comicNeueFamily,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                TextButton(onClick = {
                    navController.navigate(route = Auth) {
                        popUpTo<Splash> { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Text(
                        text = "Continue",
                        fontFamily = comicNeueFamily,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}