package com.compscicomputations.client.publik.data.model

import android.graphics.Bitmap
import java.util.Date

data class Feedback(
    val id: Int,
    val subject: String,
    val message: String,
    val suggestion: String,
    val imageBitmap: Bitmap?,
    val userEmail: String?,
    val createdAt: Date,
    val responseMessage: String?,
    val responseImageBitmap: Bitmap?,
    val respondedAt: Date?,
    val respondedByEmail: String?,
)
