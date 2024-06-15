package com.compscicomputations.presentation.auth

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthDataStore(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "register_datastore")

    private val termsAcceptedKey = booleanPreferencesKey("terms_accepted")

    val termsAcceptedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
            preferences[termsAcceptedKey] ?: false
        }

    @Composable
    fun SetTermsAccepted(accepted: Boolean = true) {
        LaunchedEffect(accepted) {
            context.dataStore.edit { preferences ->
                preferences[termsAcceptedKey] = accepted
            }
        }
    }
}

