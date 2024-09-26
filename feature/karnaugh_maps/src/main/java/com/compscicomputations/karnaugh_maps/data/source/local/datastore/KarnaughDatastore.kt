package com.compscicomputations.karnaugh_maps.data.source.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom
import com.compscicomputations.karnaugh_maps.data.model.CurrentTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class KarnaughDatastore(
    currentTab: CurrentTab
) {
    companion object {
        const val TAG = "KarnaughDatastore"
    }
    private val Context.preferences by preferencesDataStore(name = "karnaugh_maps-$currentTab-datastore")

    private val convertFromKey = stringPreferencesKey("convert_from")
    private val valueKey = stringPreferencesKey("value")

    fun lastState(context: Context): Flow<LastState?> = context.preferences.data.map { preferences ->
        val convertFrom = preferences[convertFromKey] ?: ConvertFrom.Expression.name
        Log.d(TAG, "lastState: $convertFrom, ${preferences[valueKey]}")
        LastState(ConvertFrom.valueOf(convertFrom), preferences[valueKey] ?: "")
    }

    suspend fun setLastState(context: Context, convertFrom: ConvertFrom, value: String = "") = context.preferences.edit {
        it[convertFromKey] = convertFrom.name
        it[valueKey] = value
        Log.d(TAG, "setLastState: $convertFrom, $value")
    }
    suspend fun setConvertFrom(context: Context, convertFrom: ConvertFrom, value: String) = context.preferences.edit {
        it[convertFromKey] = convertFrom.name
        it[valueKey] = value
        Log.d(TAG, "setConvertFrom: ${convertFrom.name}, $value")
    }
    suspend fun setValue(context: Context, value: String) = context.preferences.edit {
        it[valueKey] = value
        Log.d(TAG, "setValue: $value")
    }
}

object TwoVarsDataStore: KarnaughDatastore(CurrentTab.TwoVars)
object ThreeVarsDataStore: KarnaughDatastore(CurrentTab.ThreeVars)
object FourVarsDataStore: KarnaughDatastore(CurrentTab.FourVars)

suspend fun Context.setLastState(
    currentTab: CurrentTab,
    convertFrom: String,
    value: String = ""
) {
    when(currentTab) {
        CurrentTab.TwoVars -> TwoVarsDataStore.setLastState(this, ConvertFrom.valueOf(convertFrom), value)
        CurrentTab.ThreeVars ->ThreeVarsDataStore.setLastState(this, ConvertFrom.valueOf(convertFrom), value)
        CurrentTab.FourVars -> FourVarsDataStore.setLastState(this, ConvertFrom.valueOf(convertFrom), value)
    }
}
