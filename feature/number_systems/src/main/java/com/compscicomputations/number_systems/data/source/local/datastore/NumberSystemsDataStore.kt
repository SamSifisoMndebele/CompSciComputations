package com.compscicomputations.number_systems.data.source.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.CurrentTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class NumberSystemsDataStore(
    currentTab: CurrentTab
) {
    protected val Context.preferences by preferencesDataStore(name = "number_systems-$currentTab-datastore")

    private val convertFromKey = stringPreferencesKey("convert_from")
    private val fromValueKey = stringPreferencesKey("from_value")

    fun lastState(context: Context): Flow<LastState?> = context.preferences.data.map { preferences ->
        val convertFrom = preferences[convertFromKey]
        if (convertFrom.isNullOrBlank()) null
        else LastState(
            ConvertFrom.valueOf(convertFrom),
            preferences[fromValueKey] ?: ""
        )
    }

    suspend fun setLastState(context: Context, convertFrom: ConvertFrom, fromValue: String = "") = context.preferences.edit {
        it[convertFromKey] = convertFrom.toString()
        it[fromValueKey] = fromValue
    }
}

object BaseNDataStore: NumberSystemsDataStore(CurrentTab.BaseN)

object ComplementDataStore: NumberSystemsDataStore(CurrentTab.Complement)

object ExcessDataStore: NumberSystemsDataStore(CurrentTab.Excess) {
    private val bitLengthKey = intPreferencesKey("bit_length")
    fun bitLength(context: Context): Flow<Int?> = context.preferences.data.map { preferences ->
        preferences[bitLengthKey]
    }
    suspend fun setBits(context: Context, bitLength: Int) = context.preferences.edit {
        it[bitLengthKey] = bitLength
    }
}

object FloatingPointDataStore: NumberSystemsDataStore(CurrentTab.FloatingPoint)