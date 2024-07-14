package com.compscicomputations.core.ktor_client.auth.impl

import android.util.Log
import com.compscicomputations.core.ktor_client.auth.UserRepository
import com.compscicomputations.core.ktor_client.auth.UserRepository.Companion.NullAuthUserException
import com.compscicomputations.core.ktor_client.auth.models.AuthUser
import com.compscicomputations.core.ktor_client.auth.models.NewUser
import com.compscicomputations.core.ktor_client.auth.models.User
import com.compscicomputations.core.ktor_client.auth.models.Users
import com.compscicomputations.core.ktor_client.auth.models.Usertype
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val client: HttpClient) : UserRepository {
    override suspend fun getUser(): User? {
        val response = client.get(Users.Me())
        return when {
//            response.status == HttpStatusCode.NotFound -> null
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> response.body<User?>()
        }
    }

    override suspend fun createUser(user: NewUser) {
        val response = client.post(Users()) {
            contentType(ContentType.Application.Json)
            setBody(user)
        }
        if (response.status != HttpStatusCode.Created) throw Exception(response.bodyAsText())
    }
}