package com.compscicomputations.data.repository.impl

import com.compscicomputations.BuildConfig
import com.compscicomputations.data.api.Task
import com.compscicomputations.data.dto.UserDto
import com.compscicomputations.data.dto.UserDto.Companion.toDateString
import com.compscicomputations.data.model.User
import com.compscicomputations.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val postgrest: Postgrest,
    private val storage: Storage
) : UserRepository {
    override fun createUser(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String?,
        userType: String,
        createdAt: Date,
        lastSeenAt: Date
    ): Task<Unit> {
        return try { //UUID.randomUUID().toString()
            runBlocking(Dispatchers.IO) {
                val userDto = UserDto(uid, displayName, email, photoUrl, userType,
                    createdAt.toDateString(), lastSeenAt.toDateString())
                postgrest.from("user").insert(userDto)
                Task.successfulTask(Unit)
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    override fun getUsers(): Task<List<User>> {
        return try {
            runBlocking(Dispatchers.IO) {
                val result = postgrest.from("user").select().decodeList<UserDto>()
                Task.successfulTask(result.map { it.user })
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    override fun isUserSigned(): Boolean {
        return auth.currentUser != null
    }

    override fun getUser(uid: String?): Task<User> {
        return try {
            runBlocking(Dispatchers.IO) {
                val result = postgrest.from("user").select {
                    filter { eq("uid", uid ?: auth.currentUser!!.uid) }
                }.decodeSingle<UserDto>()
                Task.successfulTask(result.user)
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    override fun checkUser(uid: String?): Task<Unit> {
        return try {
            runBlocking(Dispatchers.IO) {
                postgrest.from("user").select {
                    filter { eq("uid", uid ?: auth.currentUser!!.uid) }
                }.decodeSingle<UserDto>()
                Task.successfulTask(Unit)
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    override fun logout(): Task<Unit> {
        return try {
            runBlocking(Dispatchers.IO) {
                postgrest.from("user").update({
                    set("last_seen_at", Date().toDateString())
                }) {
                    filter { eq("uid", auth.currentUser!!.uid) }
                }
                Task.successfulTask(auth.signOut())
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    override fun deleteUser(uid: String): Task<Unit> {
        return try {
            runBlocking(Dispatchers.IO) {
                postgrest.from("user").delete {
                    filter {
                        eq("uid", uid)
                    }
                }
                Task.successfulTask(Unit)
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    override fun updateUser(
        uid: String,
        displayName: String,
        email: String,
        imageFile: File?,
        userType: String,
        createdAt: Date,
        lastSeenAt: Date
    ): Task<Unit> {
        return try {
            runBlocking(Dispatchers.IO) {
                if (imageFile?.readBytes()?.isNotEmpty() == true) {
                    val imageUrl =
                        storage.from("User%20Image").upload(
                            path = "${imageFile.name}.png",
                            data = imageFile.readBytes(),
                            upsert = true
                        )
                    postgrest.from("user").update({
                        set("display_name", displayName)
                        set("email", email)
                        set("image_url", buildImageUrl(imageFileName = imageUrl))
                        set("user_type", userType)
                        set("created_at", createdAt)
                        set("last_seen_at", lastSeenAt)
                    }) {
                        filter { eq("uid", uid) }
                    }
                } else {
                    postgrest.from("user").update({
                        set("display_name", displayName)
                        set("email", email)
                        set("user_type", userType)
                        set("created_at", createdAt)
                        set("last_seen_at", lastSeenAt)
                    }) {
                        filter { eq("uid", uid) }
                    }
                }
                Task.successfulTask(Unit)
            }
        } catch (e: Exception) {
            Task.failedTask(e)
        }
    }

    // Because I named the bucket as "User Image" so when it turns to an url, it is "%20"
    // For better approach, you should create your bucket name without space symbol
    private fun buildImageUrl(imageFileName: String) =
        "${BuildConfig.SUPABASE_URL}/storage/v1/object/public/${imageFileName}".replace(" ", "%20")
}