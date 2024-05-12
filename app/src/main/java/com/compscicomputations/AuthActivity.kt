package com.compscicomputations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compscicomputations.ui.theme.CompSciComputationsTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                Surface {
                    //AuthHostScreen(this)
                    CountriesList()
                }
            }
        }
    }


    @Composable
    private fun CountriesList() {
        val supabase = createSupabaseClient(
            supabaseUrl = "https://qgidphztnqzgabsvvxmf.supabase.co",
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Postgrest)
        }
        var countries by remember { mutableStateOf<List<Country>>(listOf()) }
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
        }
    }

}

