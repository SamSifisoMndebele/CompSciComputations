package com.compscicomputations.client.auth.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object AuthDataStore {
    private const val TAG = "AuthDataStore"
    private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_datastore")

    private val firstLaunchKey = booleanPreferencesKey("first_launch")
    private val termsAcceptedKey = booleanPreferencesKey("terms_accepted")

    val Context.firstLaunchFlow: Flow<Boolean>
        get() = preferencesDataStore.data.map { preferences ->
            preferences[firstLaunchKey] ?: true
        }
    suspend fun Context.setFirstLaunch(isFirstLaunch: Boolean) = preferencesDataStore.edit { preferences ->
        preferences[firstLaunchKey] = isFirstLaunch
    }

    val Context.termsAcceptedFlow: Flow<Boolean>
        get() = preferencesDataStore.data.map { preferences ->
            preferences[termsAcceptedKey] ?: false
        }
    suspend fun Context.setTermsAccepted(isAccepted: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[termsAcceptedKey] = isAccepted
        }
    }

}