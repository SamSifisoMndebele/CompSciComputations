package com.compscicomputations.core.database.dao.impl

import android.net.Uri
import com.compscicomputations.core.database.asString
import com.compscicomputations.core.database.buildImageUrl
import com.compscicomputations.core.database.dao.UserDao
import com.compscicomputations.core.database.dao.UserDao.Companion.CURRENT_USER_IMAGE
import com.compscicomputations.core.database.dao.UserDao.Companion.CURRENT_USER_UID
import com.compscicomputations.core.database.dto.UserDto
import com.compscicomputations.core.database.model.User
import com.compscicomputations.core.database.model.UserType
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
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
        //call sql function: get_user(uid varchar(32)) returns user
        val user = postgrest.rpc("get_user", mapOf("_uid" to uidOf(uid)))
            .decodeAsOrNull<UserDto>()

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
                "_role_name" to role,
                "_code_chars" to code
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