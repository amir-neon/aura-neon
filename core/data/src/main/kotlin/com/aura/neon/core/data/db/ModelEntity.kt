package com.aura.neon.core.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aura.neon.core.domain.model.ModelInfo

@Entity(
    tableName = "models",
    indices = [Index(value = ["uri"], unique = true)],
)
data class ModelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val fileName: String,
    val sizeBytes: Long,
    val lastModified: Long,
    val enabled: Boolean = true,
    val pinned: Boolean = false,
    val note: String = "",
    val addedAt: Long,
)

fun ModelEntity.toDomain(): ModelInfo = ModelInfo(
    id = id,
    uri = uri,
    fileName = fileName,
    sizeBytes = sizeBytes,
    lastModified = lastModified,
    enabled = enabled,
    pinned = pinned,
    note = note,
    addedAt = addedAt,
)
