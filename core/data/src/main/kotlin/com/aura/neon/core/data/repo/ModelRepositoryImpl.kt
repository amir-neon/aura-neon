package com.aura.neon.core.data.repo

import android.net.Uri
import com.aura.neon.core.data.db.ModelDao
import com.aura.neon.core.data.db.ModelEntity
import com.aura.neon.core.data.db.toDomain
import com.aura.neon.core.data.scanner.GgufScanner
import com.aura.neon.core.data.scanner.LocalProviderPolicy
import com.aura.neon.core.data.settings.AuraSettings
import com.aura.neon.core.domain.model.ModelInfo
import com.aura.neon.core.domain.repository.ModelRepository
import com.aura.neon.core.domain.repository.ScanError
import com.aura.neon.core.domain.repository.ScanOutcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Room-backed model registry. Files are referenced in place via SAF URIs;
 * nothing is ever copied (spec). Permission is re-checked at scan time (R13).
 */
@Singleton
class ModelRepositoryImpl @Inject constructor(
    private val dao: ModelDao,
    private val settings: AuraSettings,
    private val scanner: GgufScanner,
) : ModelRepository {

    override val models: Flow<List<ModelInfo>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override val selectedFolder: Flow<String?> = settings.selectedFolder

    override suspend fun setSelectedFolder(uri: String?) = settings.setSelectedFolder(uri)

    override suspend fun scan(): ScanOutcome {
        val folder = settings.selectedFolder.firstOrNull()
            ?: return ScanOutcome.Failure(ScanError.NO_FOLDER)

        val treeUri = runCatching { Uri.parse(folder) }.getOrNull()
            ?: return ScanOutcome.Failure(ScanError.NO_FOLDER)

        if (LocalProviderPolicy.isNonLocalProvider(treeUri)) {
            return ScanOutcome.Failure(ScanError.NON_LOCAL_PROVIDER)
        }
        if (!scanner.hasPersistedRead(treeUri)) {
            return ScanOutcome.Failure(ScanError.PERMISSION_LOST)
        }

        val scanned = try {
            scanner.scanTree(treeUri)
        } catch (_: SecurityException) {
            return ScanOutcome.Failure(ScanError.PERMISSION_LOST)
        }
        if (scanned.isEmpty()) return ScanOutcome.Failure(ScanError.EMPTY)

        val existing = dao.allOnce().associateBy { it.uri }
        val now = System.currentTimeMillis()
        val toUpsert = scanned.map { file ->
            val ex = existing[file.uri]
            ModelEntity(
                id = ex?.id ?: 0L,
                uri = file.uri,
                fileName = file.fileName,
                sizeBytes = file.sizeBytes,
                lastModified = file.lastModified,
                enabled = ex?.enabled ?: true,
                pinned = ex?.pinned ?: false,
                note = ex?.note ?: "",
                addedAt = ex?.addedAt ?: now,
            )
        }
        dao.upsertAll(toUpsert)

        // Prune registrations whose files disappeared from the folder.
        val liveUris = scanned.map { it.uri }.toSet()
        if (liveUris.isNotEmpty()) dao.deleteNotIn(liveUris.toList())

        val added = toUpsert.count { it.id == 0L }
        return ScanOutcome.Success(found = scanned.size, added = added, updated = toUpsert.size - added)
    }

    override suspend fun setEnabled(id: Long, enabled: Boolean) = dao.setEnabled(id, enabled)
    override suspend fun setPinned(id: Long, pinned: Boolean) = dao.setPinned(id, pinned)
    override suspend fun setNote(id: Long, note: String) = dao.setNote(id, note)
    override suspend fun remove(id: Long) = dao.delete(id)
}
