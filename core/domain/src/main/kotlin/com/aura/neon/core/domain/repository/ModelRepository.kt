package com.aura.neon.core.domain.repository

import com.aura.neon.core.domain.model.ModelInfo
import kotlinx.coroutines.flow.Flow

sealed interface ScanOutcome {
    data class Success(val found: Int, val added: Int, val updated: Int) : ScanOutcome
    data class Failure(val reason: ScanError) : ScanOutcome
}

enum class ScanError {
    NO_FOLDER,
    NON_LOCAL_PROVIDER,
    PERMISSION_LOST,
    EMPTY,
}

/**
 * Model registry contract. Room-backed in core:data; the .gguf files are
 * referenced in place via SAF URIs and are never copied (spec).
 */
interface ModelRepository {
    val models: Flow<List<ModelInfo>>
    val selectedFolder: Flow<String?>

    suspend fun setSelectedFolder(uri: String?)
    suspend fun scan(): ScanOutcome

    suspend fun setEnabled(id: Long, enabled: Boolean)
    suspend fun setPinned(id: Long, pinned: Boolean)
    suspend fun setNote(id: Long, note: String)
    suspend fun remove(id: Long)
}
