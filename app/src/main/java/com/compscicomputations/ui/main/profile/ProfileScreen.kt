package com.compscicomputations.ui.main.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.compscicomputations.BuildConfig
import com.compscicomputations.R
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.navigation.Profile
import com.compscicomputations.ui.utils.CompSciScaffold
import com.compscicomputations.ui.utils.OptionButton
import com.compscicomputations.ui.utils.isLoading
import com.compscicomputations.ui.utils.rememberShimmerBrushState
import com.compscicomputations.ui.utils.shimmerBrush
import com.compscicomputations.utils.createImageFile

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    uiState: ProfileUiState,
    navigateUp: () -> Unit,
    navigateAuth: () -> Unit
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

    CompSciScaffold(
        title = "Profile",
        snackBarHost = { SnackbarHost(hostState = viewModel.snackBarHostState) },
        menuActions = {
            //TODO Menu
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
            }
        },
        isRefreshing = uiState.progressState.isLoading,
        onRefresh = { viewModel.onRefresh() },
        navigateUp = navigateUp
    ) { contentPadding ->

        var logoutAlertDialog by remember { mutableStateOf(false) }
        if (logoutAlertDialog) {
            AlertDialog(
                icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Alert Icon") },
                title = { Text(text = "LOGOUT", fontFamily = comicNeueFamily) },
                text = { Text(text = "Do you want to logout?", fontFamily = comicNeueFamily) },
                onDismissRequest = { logoutAlertDialog = false },
                confirmButton = {
                    TextButton(onClick = { viewModel.logout(); logoutAlertDialog = false; navigateAuth() }) {
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = contentPadding
        ) {
            item {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        Box(Modifier.fillMaxWidth()){
                            Row (
                                modifier = Modifier.padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val showShimmer = rememberShimmerBrushState()
                                AsyncImage(
                                    modifier = Modifier
                                        .size(180.dp)
                                        .padding(8.dp)
                                        .background(
                                            shimmerBrush(showShimmer = showShimmer.value),
                                            CircleShape
                                        )
                                        .clip(CircleShape),
                                    model = uiState.photoUrl,
                                    contentScale = ContentScale.FillBounds,
                                    onSuccess = { showShimmer.value = false },
                                    contentDescription = "Profile",
                                )
                                Column(
                                    Modifier.weight(1f)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .widthIn(min = 80.dp)
                                            .background(
                                                shimmerBrush(showShimmer = showShimmer.value),
                                                CircleShape
                                            ),
                                        text = uiState.usertype.takeIf { it != Usertype.OTHER }?.name ?: "",
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = comicNeueFamily
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        modifier = Modifier
                                            .widthIn(min = 128.dp)
                                            .background(
                                                shimmerBrush(showShimmer = showShimmer.value),
                                                CircleShape
                                            ),
                                        text = uiState.displayName,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = comicNeueFamily
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        modifier = Modifier
                                            .widthIn(min = 180.dp)
                                            .background(
                                                shimmerBrush(showShimmer = showShimmer.value),
                                                CircleShape
                                            ),
                                        text = uiState.email,
                                        fontSize = 14.sp,
                                    )
                                }
                                /*var imageExpanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                expanded = imageExpanded,
                                onExpandedChange = { imageExpanded = !imageExpanded }
                            ) {
                                OutlinedButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                        .padding(vertical = 4.dp),
                                ) {
                                    Text(text = "Take a new picture.")
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

                            }*/
                                /*TextButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .padding(end = 4.dp, start = 6.dp)
                                        .weight(1f)
                                ) {
                                    Text(text = "Change Image", )
                                }*/
                            }
                        }
                    }

                    OptionButton(
                        padding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
                        iconVector = Icons.Default.Person2,
                        text = "title",
                        onClick = {

                        }
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = uiState.user.toString()
                    )

                }
            }
            item { 
                OptionButton(
                    text = "Logout",
                    iconVector = Icons.AutoMirrored.Filled.Logout,
                ) {
                    logoutAlertDialog = true
                }
            }
        }
    }
}