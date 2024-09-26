package com.compscicomputations.karnaugh_maps.data.source.local.datastore

import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom

data class LastState(
    val convertFrom: ConvertFrom,
    val value: String
)
