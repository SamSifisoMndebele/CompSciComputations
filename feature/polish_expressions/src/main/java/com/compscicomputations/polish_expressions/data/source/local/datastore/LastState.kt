package com.compscicomputations.polish_expressions.data.source.local.datastore

import com.compscicomputations.polish_expressions.data.model.ConvertFrom

data class LastState(
    val convertFrom: ConvertFrom,
    val fromValue: String
)