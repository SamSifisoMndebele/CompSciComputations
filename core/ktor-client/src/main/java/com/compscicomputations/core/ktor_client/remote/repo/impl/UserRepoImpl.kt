package com.compscicomputations.core.ktor_client.remote.repo.impl

import com.compscicomputations.core.ktor_client.buildImageUrl
import com.compscicomputations.core.ktor_client.remote.repo.UserRepo
import com.compscicomputations.core.ktor_client.remote.repo.UserRepo.Companion.USER_IMAGE
import com.compscicomputations.core.ktor_client.remote.repo.UserRepo.Companion.USER_UID
import com.compscicomputations.core.ktor_client.model.User
import com.compscicomputations.core.ktor_client.model.Usertype
import com.compscicomputations.core.ktor_client.network.ConnectionState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.Date
import javax.inject.Inject


class UserRepoImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectionState: ConnectionState
) : UserRepo {
    private val String?.checkedUID : String
        get() = when(this) {
            null -> throw Exception("UID is null.")
            USER_UID -> auth.currentUser!!.uid
            else -> this.replace(Regex("[\"']"), "")
        }

    override suspend fun getUser(uid: String): User? {
//        return when(connectionState) {
//            ConnectionState.Available -> postgrest.rpc("get_user", mapOf("_uid" to uid.checkedUID))
//                .decodeAsOrNull<User>()
//            ConnectionState.Unavailable -> throw Exception("No internet connection.")
//        }
        TODO("Not yet implemented")
    }

    override suspend fun insertUser(
        uid: String,
        displayName: String,
        email: String,
        phone: String?,
        photoUrl: String?
    ) {
        //call sql function: upsert_user
//        postgrest.rpc("upsert_user", mapOf(
//            "_uid" to uid.checkedUID,
//            "_phone" to phone,
////            "_usertype" to "Student",
////            "_is_admin" to false
//        ))
        TODO("Not yet implemented")
    }

     suspend fun insertAdminUser(
        uid: String,
        displayName: String,
        email: String,
        phone: String?,
        photoUrl: String?,
        role: String,
        code: String
    ) {
        //call sql function: insert_admin_user
//        postgrest.rpc("insert_admin_user",
//            mapOf(
//                "_uid" to uid.checkedUID,
//                "_displayName" to displayName,
//                "_email" to email,
//                "_phone" to phone,
//                "_photoUrl" to photoUrl,
//                "_role_name" to role,
//                "_code_chars" to code
//            )
//        )
         TODO("Not yet implemented")
    }

     suspend fun insertStudentUser(
        uid: String,
        displayName: String,
        email: String,
        phone: String?,
        photoUrl: String?,
        course: String,
        school: String
    ) {
        //call sql function: insert_student_user
//        postgrest.rpc("insert_student_user",
//            mapOf(
//                "_uid" to uid.checkedUID,
//                "_displayName" to displayName,
//                "_email" to email,
//                "_phone" to phone,
//                "_photoUrl" to photoUrl,
//                "_course" to course,
//                "_school" to school
//            )
//        )
         TODO("Not yet implemented")
    }

    override suspend fun deleteUser(uid: String) {
//        postgrest.from("user").delete {
//            filter { User::uid eq uid.checkedUID }
//        }
        auth.currentUser!!.delete().await()
    }

    override suspend fun updateUser(
        uid: String,
        displayName: String,
        imageFile: File?,
        usertype: Usertype,
        lastSeenAt: Date
    ) {
        if (imageFile != USER_IMAGE) {
//            val imageUrl = if(imageFile?.readBytes()?.isNotEmpty() == true) {
//                val image = storage.from("User%20Image").upload(
//                    path = "${imageFile.name}.png",
//                    data = imageFile.readBytes(),
//                    upsert = true
//                )
//                buildImageUrl(image)
//            } else {
//                null
//            }
//            postgrest.from("user").update({
//                User::photoUrl setTo imageUrl
//                User::displayName setTo displayName
//                User::usertype setTo usertype.sqlName
//            }) {
//                filter { User::uid eq uid.checkedUID }
//            }
        } else {
//            postgrest.from("user").update({
//                User::displayName setTo displayName
//                User::usertype setTo usertype.sqlName
//            }) {
//                filter { User::uid eq uid.checkedUID }
//            }
        }
        TODO("Not yet implemented")
    }

    override suspend fun updateUserDisplayName(uid: String, displayName: String) {
//        postgrest.from("user").update({
//            User::displayName setTo displayName
//        }) {
//            filter { User::uid eq uid.checkedUID }
//        }
        TODO("Not yet implemented")
    }

    override suspend fun updateUserImage(uid: String, imageFile: File?) {
//        val imageUrl = if(imageFile?.readBytes()?.isNotEmpty() == true) {
//            val image = storage.from("User%20Image").upload(
//                path = "${imageFile.name}.png",
//                data = imageFile.readBytes(),
//                upsert = true
//            )
//            buildImageUrl(image)
//        } else {
//            null
//        }
//        postgrest.from("user").update({
//            User::photoUrl setTo imageUrl
//        }) {
//            filter { User::uid eq uid.checkedUID }
//        }
        TODO("Not yet implemented")
    }

    override suspend fun updateUserUserType(uid: String, usertype: Usertype) {
//        postgrest.from("user").update({
//            User::usertype setTo usertype.sqlName
//        }) {
//            filter { User::uid eq uid.checkedUID }
//        }
        TODO("Not yet implemented")
    }

    override suspend fun updateUserLastSeen(uid: String) {
//        postgrest.rpc("update_last_seen", mapOf("_uid" to uid.checkedUID))
        TODO("Not yet implemented")
    }
}