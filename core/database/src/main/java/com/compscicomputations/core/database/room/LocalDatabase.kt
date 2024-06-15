package com.compscicomputations.core.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.compscicomputations.core.database.room.dao.UserDao
import com.compscicomputations.core.database.model.User
import com.compscicomputations.core.database.model.UserMetadata

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}