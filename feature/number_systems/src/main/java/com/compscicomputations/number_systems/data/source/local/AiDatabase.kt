package com.compscicomputations.number_systems.data.source.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AiResponse::class], version = 3, exportSchema = false)
abstract class AiDatabase : RoomDatabase() {
    abstract fun aiResponseDao(): AiResponseDao
}