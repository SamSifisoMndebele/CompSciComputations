package com.compscicomputations.core.database.auth.impl

import com.compscicomputations.core.database.auth.UserRepository
import com.compscicomputations.core.database.auth.models.NewUser
import com.compscicomputations.core.database.auth.models.User
import com.compscicomputations.core.database.auth.models.Users
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UserRepositoryImpl(
    private val client: HttpClient
) : UserRepository {
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