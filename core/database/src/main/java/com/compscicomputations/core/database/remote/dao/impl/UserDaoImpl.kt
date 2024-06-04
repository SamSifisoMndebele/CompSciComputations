package com.compscicomputations.core.database.remote.dao.impl

import com.compscicomputations.core.database.buildImageUrl
import com.compscicomputations.core.database.remote.dao.UserDao
import com.compscicomputations.core.database.remote.dao.UserDao.Companion.CURRENT_USER_IMAGE
import com.compscicomputations.core.database.remote.dao.UserDao.Companion.CURRENT_USER_UID
import com.compscicomputations.core.database.remote.dto.UserDto
import com.compscicomputations.core.database.remote.model.User
import com.compscicomputations.core.database.remote.model.UserType
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.Date
import javax.inject.Inject


class UserDaoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val postgrest: Postgrest,
    private val storage: Storage
) : UserDao {
    private fun uidOf(uid: String): String = if (uid == CURRENT_USER_UID)
        auth.currentUser!!.uid else uid.replace(Regex("[\"']"), "")

    override suspend fun getUser(uid: String): User? {
        val user = postgrest.from("user").select {
            filter {
                UserDto::uid eq uidOf(uid)
            }
        }.decodeSingleOrNull<UserDto>()
        return user?.asUser
    }

    override suspend fun insertUser(
        uid: String,
        displayName: String,
        email: String,
        phone: String?,
        photoUrl: String?
    ) {
        //call sql function: insert_user
        postgrest.rpc("insert_user", mapOf(
            "_uid" to uidOf(uid),
            "_displayName" to displayName,
            "_email" to email,
            "_phone" to phone,
            "_photoUrl" to photoUrl,
            "_userType" to "Student"
        ))
    }

    override suspend fun insertAdminUser(
        uid: String,
        displayName: String,
        email: String,
        phone: String?,
        photoUrl: String?,
        role: String,
        code: String
    ) {
        //call sql function: insert_admin_user
        postgrest.rpc("insert_admin_user",
            mapOf(
                "_uid" to uidOf(uid),
                "_displayName" to displayName,
                "_email" to email,
                "_phone" to phone,
                "_photoUrl" to photoUrl,
                "_role_name" to role,
                "_code_chars" to code
            )
        )
    }

    override suspend fun insertStudentUser(
        uid: String,
        displayName: String,
        email: String,
        phone: String?,
        photoUrl: String?,
        course: String,
        school: String
    ) {
        //call sql function: insert_student_user
        postgrest.rpc("insert_student_user",
            mapOf(
                "_uid" to uidOf(uid),
                "_displayName" to displayName,
                "_email" to email,
                "_phone" to phone,
                "_photoUrl" to photoUrl,
                "_course" to course,
                "_school" to school
            )
        )
    }

    override suspend fun deleteUser(uid: String) {
        postgrest.from("user").delete {
            filter { UserDto::uid eq uidOf(uid) }
        }
        auth.currentUser!!.delete().await()
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
                UserDto::photoUrl setTo imageUrl
                UserDto::displayName setTo displayName
                UserDto::userType setTo userType.sqlName
            }) {
                filter { UserDto::uid eq uidOf(uid) }
            }
        } else {
            postgrest.from("user").update({
                UserDto::displayName setTo displayName
                UserDto::userType setTo userType.sqlName
            }) {
                filter { UserDto::uid eq uidOf(uid) }
            }
        }
    }

    override suspend fun updateUserDisplayName(uid: String, displayName: String) {
        postgrest.from("user").update({
            UserDto::displayName setTo displayName
        }) {
            filter { UserDto::uid eq uidOf(uid) }
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
            UserDto::photoUrl setTo imageUrl
        }) {
            filter { UserDto::uid eq uidOf(uid) }
        }
    }

    override suspend fun updateUserUserType(uid: String, userType: UserType) {
        postgrest.from("user").update({
            UserDto::userType setTo userType.sqlName
        }) {
            filter { UserDto::uid eq uidOf(uid) }
        }
    }

    override suspend fun updateUserLastSignIn(uid: String) {
        postgrest.rpc("update_last_sign_in", mapOf("_uid" to uidOf(uid)))
    }

    override suspend fun updateUserLastSeen(uid: String) {
        postgrest.rpc("update_last_seen", mapOf("_uid" to uidOf(uid)))
    }
}