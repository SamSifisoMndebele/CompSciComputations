package com.compscicomputations.services.auth.models

import java.sql.Timestamp

data class User(
    val uid: String,
    val email: String,
    val phone: String?,
    val usertype: Usertype,
    val createdAt: Timestamp,
    val updatedAt: Timestamp?,
    val lastSeenAt: Timestamp?,
    val bannedUntil: Timestamp?,
    val deletedAt: Timestamp?,
)
