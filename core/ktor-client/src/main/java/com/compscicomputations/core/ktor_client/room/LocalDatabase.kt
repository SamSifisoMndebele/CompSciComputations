package com.compscicomputations.core.ktor_client.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compscicomputations.core.ktor_client.room.dao.UserDao
import com.compscicomputations.core.ktor_client.model.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}