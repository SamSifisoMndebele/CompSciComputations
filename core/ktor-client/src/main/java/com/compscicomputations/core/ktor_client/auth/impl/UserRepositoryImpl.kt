package com.compscicomputations.core.ktor_client.auth.impl

import android.util.Log
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.core.ktor_client.auth.models.AuthUser
import com.compscicomputations.core.ktor_client.auth.models.User
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.Response

internal class UserRepositoryImpl(private val auth: FirebaseAuth, private val client: HttpClient) : UserRepository {
    override fun getAuthUser(): AuthUser? {
        return auth.currentUser?.let {
            AuthUser(
                uid = it.uid,
                email = it.email!!,
                displayName = it.displayName!!,
                isEmailVerified = it.isEmailVerified,
                photoUrl = it.photoUrl?.toString(),
                phoneNumber = it.phoneNumber,
                usertypeFlow = flow {
                    val token = it.getIdToken(false).await()
                    Log.d("AccessToken", token.token.toString())
                    emit(Usertype.valueOf(token.claims["usertype"] as String))
                }.catch { e ->
                    emit(Usertype.OTHER)
                    if (e !is NullPointerException)
                        Log.e("UserRepositoryImpl", "usertypeFlow::error", e)
                }
            )
        }
    }

    override suspend fun getUser(): User? {
        val response = client.get("/user")
        if (response.status == HttpStatusCode.NotFound)
            return null
        if (response.status == HttpStatusCode.ExpectationFailed)
            throw Exception(response.body<String>())

        return response.body<User?>()
    }
}