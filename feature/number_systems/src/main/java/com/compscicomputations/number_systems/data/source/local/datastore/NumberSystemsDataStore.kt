package com.compscicomputations.number_systems.data.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.CurrentTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class NumberSystemsDataStore(
    protected val context: Context,
    currentTab: CurrentTab
) {
    protected val Context.preferences by preferencesDataStore(name = "number_systems-${currentTab.name}-datastore")

    private val convertFromKey = stringPreferencesKey("convert_from")
    private val fromValueKey = stringPreferencesKey("from_value")

    val lastState: Flow<LastState?>
        get() = context.preferences.data.map { preferences ->
            val convertFrom = preferences[convertFromKey]
            if (convertFrom.isNullOrBlank()) null
            else LastState(
                ConvertFrom.valueOf(convertFrom),
                preferences[fromValueKey] ?: ""
            )
        }

    suspend fun setLastState(convertFrom: ConvertFrom, fromValue: String = "") = context.preferences.edit {
        it[convertFromKey] = convertFrom.name
        it[fromValueKey] = fromValue
    }
}

class BaseNDataStore(context: Context): NumberSystemsDataStore(context, CurrentTab.BaseN)
class ComplementDataStore(context: Context): NumberSystemsDataStore(context, CurrentTab.Complement)
class ExcessDataStore(context: Context): NumberSystemsDataStore(context, CurrentTab.Excess) {
    private val bitLengthKey = intPreferencesKey("bit_length")
    val bitLength: Flow<Int?>
        get() = context.preferences.data.map { preferences ->
            preferences[bitLengthKey]
        }
    suspend fun setBits(bitLength: Int) = context.preferences.edit {
        it[bitLengthKey] = bitLength
    }
}
class FloatingPointDataStore(context: Context): NumberSystemsDataStore(context, CurrentTab.FloatingPoint)