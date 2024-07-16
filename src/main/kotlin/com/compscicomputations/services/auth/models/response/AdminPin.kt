package com.compscicomputations.services.auth.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminPin(
    val id: Int,
    val email: String,
    @SerialName("pin_hash")
    val pinHash: String
)

//  id int primary key generated always as identity,
//  email text unique not null,
//  pin_hash text not null