package com.compscicomputations.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AiResponse::class], version = 1, exportSchema = false)
abstract class AiDatabase : RoomDatabase() {
    abstract fun aiResponseDao(): AiResponseDao
}