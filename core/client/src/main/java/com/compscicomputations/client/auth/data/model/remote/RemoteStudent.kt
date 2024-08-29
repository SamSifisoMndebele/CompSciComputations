package com.compscicomputations.client.auth.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteStudent(
    val id: String,
    val university: String,
    val school: String,
    val course: String,
)
