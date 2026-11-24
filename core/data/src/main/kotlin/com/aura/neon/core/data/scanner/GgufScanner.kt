package com.aura.neon.core.data.scanner

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SAF scanner (spec R13/R14): lists .gguf documents in the selected tree,
 * reads ONLY metadata columns - no file bytes, no hash, no copy.
 * Single folder level (the user picks a dedicated models folder).
 *
 * Uses ContentResolver + DocumentsContract directly: zero extra dependencies
 * (androidx.documentfile deliberately not added to keep the pin list minimal).
 */
@Singleton
class GgufScanner @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    data class ScannedFile(
        val uri: String,
        val fileName: String,
        val sizeBytes: Long,
        val lastModified: Long,
    )

    /** Safety cap: stop listing after this many documents in one folder. */
    private val maxFiles = 1000

    fun hasPersistedRead(treeUri: Uri): Boolean =
        context.contentResolver.persistedUriPermissions.any {
            it.uri == treeUri && it.isReadPermission
        }

    /** @throws SecurityException if the persisted grant is gone (repo maps it to PERMISSION_LOST) */
    fun scanTree(treeUri: Uri): List<ScannedFile> {
        val rootDocId = DocumentsContract.getTreeDocumentId(treeUri)
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, rootDocId)
        val projection = arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
        )
        val found = mutableListOf<ScannedFile>()
        context.contentResolver.query(childrenUri, projection, null, null, null)?.use { cursor ->
            while (cursor.moveToNext() && found.size < maxFiles) {
                val docId = cursor.getString(0) ?: continue
                val name = cursor.getString(1) ?: continue
                if (!name.endsWith(".gguf", ignoreCase = true)) continue
                val size = cursor.getLong(2)
                val lastModified = cursor.getLong(3)
                val docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, docId)
                found += ScannedFile(
                    uri = docUri.toString(),
                    fileName = name,
                    sizeBytes = size,
                    lastModified = lastModified,
                )
            }
        }
        return found
    }
}
