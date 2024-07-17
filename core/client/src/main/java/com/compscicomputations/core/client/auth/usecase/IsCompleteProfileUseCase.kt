package com.compscicomputations.core.client.auth.usecase

import android.content.Context
import android.util.Log
import com.compscicomputations.core.client.auth.AuthDataStore.profileComplete
import com.compscicomputations.core.client.auth.AuthDataStore.setProfileComplete
import com.compscicomputations.core.client.auth.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class IsCompleteProfileUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Boolean> = flow {
        context.profileComplete.last().let {
            Log.d("IsCompleteProfile", "profileComplete: " +it.toString())
//            if (it != null) emit(it)
//            else {
//                val user = userRepository.getUser()
//                context.setProfileComplete(user != null)
//                emit(user != null)
//            }
            val user = userRepository.getUser()
            context.setProfileComplete(user != null)
            Log.d("IsCompleteProfile", "user: " +user.toString())
            emit(user != null)
        }
    }.retry(3).catch {
        Log.e("IsCompleteProfile", it.message.orEmpty(), it)
        emit(false)
    }.flowOn(Dispatchers.IO)

}