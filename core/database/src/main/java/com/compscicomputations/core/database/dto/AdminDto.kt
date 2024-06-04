package com.compscicomputations.core.database.dto

import kotlinx.serialization.SerialName

data class AdminDto(
    @SerialName("uid") val uid: String,
    @SerialName("admin_role") val adminRole: String?,
    @SerialName("admin_code") val adminCode: String?,
)
