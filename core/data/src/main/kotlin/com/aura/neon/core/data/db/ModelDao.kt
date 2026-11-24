package com.aura.neon.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ModelDao {

    @Query("SELECT * FROM models ORDER BY pinned DESC, fileName COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<ModelEntity>>

    @Query("SELECT * FROM models ORDER BY pinned DESC, fileName COLLATE NOCASE ASC")
    suspend fun allOnce(): List<ModelEntity>

    @Upsert
    suspend fun upsertAll(models: List<ModelEntity>)

    @Query("UPDATE models SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean)

    @Query("UPDATE models SET pinned = :pinned WHERE id = :id")
    suspend fun setPinned(id: Long, pinned: Boolean)

    @Query("UPDATE models SET note = :note WHERE id = :id")
    suspend fun setNote(id: Long, note: String)

    @Query("DELETE FROM models WHERE id = :id")
    suspend fun delete(id: Long)

    /** Prune registrations whose files vanished from the scanned folder. */
    @Query("DELETE FROM models WHERE uri NOT IN (:uris)")
    suspend fun deleteNotIn(uris: List<String>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(models: List<ModelEntity>)
}
