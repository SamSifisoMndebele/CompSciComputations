package com.compscicomputations.number_systems.data.source.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey
import com.compscicomputations.number_systems.data.model.ConvertFrom

data class LastState(
    val convertFrom: ConvertFrom,
    val fromValue: String
)