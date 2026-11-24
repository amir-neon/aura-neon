package com.aura.neon.core.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aura.neon.core.domain.model.ModelInfo
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ModelDaoTest {

    private lateinit var db: AuraDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AuraDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun entity(uri: String, name: String, size: Long = 100L, lastModified: Long = 1L) =
        ModelEntity(
            uri = uri,
            fileName = name,
            sizeBytes = size,
            lastModified = lastModified,
            enabled = true,
            pinned = false,
            note = "",
            addedAt = 1L,
        )

    @Test
    fun `order is pinned first then file name`() = runTest {
        val dao = db.modelDao()
        dao.insertAll(
            listOf(
                entity(uri = "uri-a", name = "b-model.gguf"),
                entity(uri = "uri-b", name = "a-model.gguf"),
            )
        )
        assertEquals(listOf("a-model.gguf", "b-model.gguf"), dao.allOnce().map { it.fileName })

        dao.setPinned(dao.allOnce().first { it.fileName == "b-model.gguf" }.id, true)
        assertEquals("b-model.gguf", dao.allOnce().first().fileName)
    }

    @Test
    fun `upsertAll refreshes size and lastModified without duplicating`() = runTest {
        val dao = db.modelDao()
        dao.insertAll(listOf(entity(uri = "uri-a", name = "m.gguf", size = 100)))
        val original = dao.allOnce().single()
        assertEquals(100L, original.sizeBytes)

        dao.upsertAll(
            listOf(
                original.copy(sizeBytes = 200, lastModified = 555),
            )
        )
        val updated = dao.allOnce().single()
        assertEquals(200L, updated.sizeBytes)
        assertEquals(555L, updated.lastModified)
    }

    @Test
    fun `deleteNotIn prunes vanished files only`() = runTest {
        val dao = db.modelDao()
        dao.insertAll(
            listOf(
                entity(uri = "uri-a", name = "a.gguf"),
                entity(uri = "uri-b", name = "b.gguf"),
                entity(uri = "uri-c", name = "c.gguf"),
            )
        )
        dao.deleteNotIn(listOf("uri-a", "uri-c"))
        assertEquals(listOf("a.gguf", "c.gguf"), dao.allOnce().map { it.fileName })
    }

    @Test
    fun `toggles and note persist`() = runTest {
        val dao = db.modelDao()
        dao.insertAll(listOf(entity(uri = "uri-a", name = "a.gguf")))
        val id = dao.allOnce().single().id

        dao.setEnabled(id, false)
        dao.setPinned(id, true)
        dao.setNote(id, "daily driver")

        val after = dao.allOnce().single()
        assertEquals(false, after.enabled)
        assertEquals(true, after.pinned)
        assertEquals("daily driver", after.note)
    }

    @Test
    fun `domain mapping roundtrips`() {
        val e = entity(uri = "u", name = "qwen2.5-7b-q4_k_m.gguf")
        val d: ModelInfo = e.toDomain()
        assertEquals(e.uri, d.uri)
        assertEquals(e.fileName, d.fileName)
        assertEquals(e.sizeBytes, d.sizeBytes)
        assertEquals(false, d.pinned)
    }
}
