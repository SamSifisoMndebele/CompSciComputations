package com.compscicomputations.ui.main.profile

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.compscicomputations.BuildConfig
import com.compscicomputations.R
import com.compscicomputations.ui.main.AppBar
import com.compscicomputations.ui.main.OptionButton
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.utils.createImageFile

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateAuth: () -> Unit
) {
    val userSignedOut by viewModel.userSignedOut.collectAsState()
    LaunchedEffect(userSignedOut) {
        if (userSignedOut) {
            navigateAuth()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
//        onEvent(RegisterUiEvent.OnImageUriChange(uri))
    }
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (!it) return@rememberLauncherForActivityResult
//        else onEvent(RegisterUiEvent.OnImageUriChange(uri))
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) cameraLauncher.launch(uri)
        else Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }

    val userType by viewModel.userType.collectAsState()
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()

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

    Column(
        modifier = Modifier.padding(padding),
    ) {
        AppBar(title = "Profile", navigateUp = navigateUp) {
            //TODO Menu
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                Card(
                    modifier = Modifier.padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        Row (
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                modifier = Modifier
                                    .size(180.dp)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                model = photoUrl,
                                contentScale = ContentScale.FillBounds,
                                loading = placeholder(R.drawable.img_profile),
                                failure = placeholder(R.drawable.img_profile),
                                transition = CrossFade,
                                contentDescription = "Profile"
                            )
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
                    val optionPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    OptionButton(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_number_64),
                        text = displayName,
                        onClick = {  }
                    )
                    OptionButton(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_abc),
                        text = email,
                        onClick = {  }
                    )
                    OptionButton(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_grid),
                        text = userType.name,
                        onClick = {  }
                    )

                }
            }
            item {
                OptionButton(
                    padding = PaddingValues(vertical = 8.dp),
                    iconVector = Icons.AutoMirrored.Filled.Logout,
                    text = "Logout",
                    onClick = { logoutAlertDialog = true }
                )
            }
        }
    }



}