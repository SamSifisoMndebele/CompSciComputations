package com.compscicomputations.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compscicomputations.core.database.model.Usertype.Companion.asUsertype
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class User(
    @PrimaryKey val uid: String,
    val email: String? = null,
    val phone: String? = null,
    val usertype: String = "Student",
    @SerialName("is_admin")
    @ColumnInfo(name = "is_admin")
    val admin: Boolean = false,
    @Embedded
    val metadata: UserMetadata
) {
    val getUsertype get() = usertype.asUsertype
}

