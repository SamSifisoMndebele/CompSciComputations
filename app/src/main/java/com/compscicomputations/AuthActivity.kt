package com.compscicomputations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.compscicomputations.ui.auth.AuthHostScreen
import com.compscicomputations.ui.theme.CompSciComputationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                Surface {
                    AuthHostScreen(this)
//                    CountriesList()
                }
            }
        }
    }


    @Composable
    private fun CountriesList() {

        /*var countries by remember { mutableStateOf<List<Country>>(listOf()) }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                countries = supabase.from("countries")
                    .select().decodeList<Country>()
            }
        }
        LazyColumn {
            items(
                countries,
                key = { country -> country.id },
            ) { country ->
                Text(
                    country.name,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }*/
    }

}

