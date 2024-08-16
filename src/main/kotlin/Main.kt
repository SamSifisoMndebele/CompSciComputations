import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import theme.APP_NAME

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 1280.dp, height = 640.dp),
        title = APP_NAME,
        icon = painterResource("img/ic_launcher.png"),
    ) {
//
        var action by remember { mutableStateOf("Hello, World!") }

        MenuBar {
            Menu("Tasks", mnemonic = 'T') {
                Item(
                    "Create new Task",
                    onClick = { action = "Last action: Create new Task" },
                    shortcut = KeyShortcut(Key.C, ctrl = true)
                )
            }
        }
        Row {
            NavigationRail {
                repeat(5) {
                    NavigationRailItem(
                        selected = true,
                        onClick = { action = "Last action: Create new Task" },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Icon"
                            )
                        },
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
            ) {
                TextButton(onClick = {}, modifier = Modifier.padding(8.dp)) {
                    Text(text = "Change View")
                }
                Text(text = action)
                App()
            }
        }


    }
}
