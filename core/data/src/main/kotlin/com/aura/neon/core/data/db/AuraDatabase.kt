package com.aura.neon.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ModelEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AuraDatabase : RoomDatabase() {
    abstract fun modelDao(): ModelDao

    companion object {
        const val NAME = "aura.db"
    }
}
