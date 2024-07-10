package com.compscicomputations.ui.main.usecase

import android.app.Activity
import android.util.Log
import com.compscicomputations.core.ktor_client.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Unit> = flow {
        emit(authRepository.login(email, password))
    }/*.catch {
        Log.e("Login Error", it.message.orEmpty())
    }*/.flowOn(Dispatchers.IO)

    operator fun invoke(activity: Activity): Flow<Unit> = flow {
        emit(authRepository.loginWithGoogle(activity))
    }/*.catch {
        Log.e("Login Error", it.message.orEmpty())
    }*/.flowOn(Dispatchers.IO)

}