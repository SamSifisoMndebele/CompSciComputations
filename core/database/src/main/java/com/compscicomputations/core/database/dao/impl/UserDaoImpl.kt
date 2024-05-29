package com.compscicomputations.core.database.dao.impl

import com.compscicomputations.core.database.UserType
import com.compscicomputations.core.database.asString
import com.compscicomputations.core.database.buildImageUrl
import com.compscicomputations.core.database.dao.UserDao
import com.compscicomputations.core.database.dao.UserDao.Companion.CURRENT_USER_IMAGE
import com.compscicomputations.core.database.dao.UserDao.Companion.CURRENT_USER_UID
import com.compscicomputations.core.database.dto.UserDto
import com.compscicomputations.core.database.model.User
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import java.io.File
import java.util.Date
import javax.inject.Inject

class UserDaoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val postgrest: Postgrest,
    private val storage: Storage
) : UserDao {

    override suspend fun getUser(uid: String): User? {
        val userId = if (uid == CURRENT_USER_UID) auth.currentUser!!.uid else uid
        val user = postgrest.from("user").select {
            filter { eq("uid", userId) }
        }.decodeSingleOrNull<UserDto>()
        return user?.asUser
    }

    override suspend fun getUsers(): List<User> {
        val users = postgrest.from("user").select().decodeList<UserDto>()
        return users.map { user -> user.asUser }
    }

    override suspend fun insertUser(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String?,
        userType: UserType,
        createdAt: Date,
        lastSeenAt: Date
    ) {
        val userDto = UserDto(
            uid,
            displayName,
            email,
            photoUrl,
            userType.name,
            createdAt.asString,
            lastSeenAt.asString
        )
        postgrest.from("user").insert(userDto)
    }


    override suspend fun deleteUser(uid: String) {
        postgrest.from("user").delete {
            filter {
                eq("uid", uid)
            }
        }
//        auth. todo delete on firebase
    }

    override suspend fun updateUser(
        uid: String,
        displayName: String,
        imageFile: File?,
        userType: UserType,
        lastSeenAt: Date
    ) {
        if (imageFile != CURRENT_USER_IMAGE) {
            val imageUrl = if(imageFile?.readBytes()?.isNotEmpty() == true) {
                val image = storage.from("User%20Image").upload(
                    path = "${imageFile.name}.png",
                    data = imageFile.readBytes(),
                    upsert = true
                )
                buildImageUrl(image)
            } else {
                null
            }
            postgrest.from("user").update({
                set("display_name", displayName)
                set("image_url", imageUrl)
                set("user_type", userType)
                set("last_seen_at", lastSeenAt)
            }) {
                filter { eq("uid", uid) }
            }
        } else {
            postgrest.from("user").update({
                set("display_name", displayName)
                set("user_type", userType)
                set("last_seen_at", lastSeenAt)
            }) {
                filter { eq("uid", uid) }
            }
        }
    }

    override suspend fun updateUserDisplayName(uid: String, displayName: String) {
        postgrest.from("user").update({
            set("display_name", displayName)
        }) {
            filter { eq("uid", uid) }
        }
    }

    override suspend fun updateUserImage(uid: String, imageFile: File?) {
        val imageUrl = if(imageFile?.readBytes()?.isNotEmpty() == true) {
            val image = storage.from("User%20Image").upload(
                path = "${imageFile.name}.png",
                data = imageFile.readBytes(),
                upsert = true
            )
            buildImageUrl(image)
        } else {
            null
        }
        postgrest.from("user").update({
            set("image_url", imageUrl)
        }) {
            filter { eq("uid", uid) }
        }
    }

    override suspend fun updateUserUserType(uid: String, userType: UserType) {
        postgrest.from("user").update({
            set("user_type", userType)
        }) {
            filter { eq("uid", uid) }
        }
    }

    override suspend fun updateUserLastSeen(uid: String, lastSeenAt: Date) {
        postgrest.from("user").update({
            set("last_seen_at", lastSeenAt)
        }) {
            filter { eq("uid", uid) }
        }
    }
}