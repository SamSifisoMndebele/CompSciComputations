package com.compscicomputations.polish_expressions.data.source.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PolishDataStore {
    private val Context.preferences by preferencesDataStore(name = "polish_expressions-datastore")

    private val convertFromKey = stringPreferencesKey("convert_from")
    private val fromValueKey = stringPreferencesKey("from_value")

    fun lastState(context: Context): Flow<LastState?> = context.preferences.data.map { preferences ->
        val convertFrom = preferences[convertFromKey]
        if (convertFrom.isNullOrBlank()) null
        else LastState(
            convertFrom = ConvertFrom.valueOf(convertFrom),
            fromValue = preferences[fromValueKey] ?: ""
        )
    }

    suspend fun setLastState(context: Context, convertFrom: ConvertFrom, fromValue: String = "") = context.preferences.edit {
        it[convertFromKey] = convertFrom.toString()
        it[fromValueKey] = fromValue
    }
}