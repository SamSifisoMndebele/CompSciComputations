package com.compscicomputations.core.database.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object AuthDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_datastore")

    private val firstLaunchKey = booleanPreferencesKey("first_launch")
    private val termsAcceptedKey = booleanPreferencesKey("terms_accepted")
    private fun profileCompleteKey(uid: String?) = booleanPreferencesKey(uid ?: "profile_complete")

    val Context.firstLaunchFlow: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[firstLaunchKey] ?: true
        }
    suspend fun Context.setFirstLaunch(isFirstLaunch: Boolean) = dataStore.edit { preferences ->
        preferences[firstLaunchKey] = isFirstLaunch
    }

    val Context.termsAcceptedFlow: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[termsAcceptedKey] ?: false
        }
    suspend fun Context.setTermsAccepted(isAccepted: Boolean) = dataStore.edit { preferences ->
        preferences[termsAcceptedKey] = isAccepted
    }

    val Context.profileComplete: Flow<Boolean?>
        get() = dataStore.data.map { preferences ->
//            preferences[profileCompleteKey(Firebase.auth.currentUser?.uid)]
            TODO()
        }
    suspend fun Context.setProfileComplete(isComplete: Boolean) = dataStore.edit { preferences ->
//        preferences[profileCompleteKey(Firebase.auth.currentUser?.uid)] = isComplete
        TODO()
    }

}