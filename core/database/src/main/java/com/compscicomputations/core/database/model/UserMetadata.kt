package com.compscicomputations.core.database.model

import java.util.Date

data class UserMetadata(
    val uid: String,
    val createdAt: Date,
    val lastSignInAt: Date,
    val updatedAt: Date? = null,
    val lastSeenAt: Date? = null,
    val bannedUntil: Date? = null,
    val deletedAt: Date? = null
)
