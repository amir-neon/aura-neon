package com.aura.neon.core.domain.model

/**
 * A registered .gguf model. The file itself is NEVER copied anywhere (spec):
 * identity = URI + size + lastModified (spec R14), all captured at scan time.
 */
data class ModelInfo(
    val id: Long,
    val uri: String,
    val fileName: String,
    val sizeBytes: Long,
    val lastModified: Long,
    val enabled: Boolean = true,
    val pinned: Boolean = false,
    val note: String = "",
    val addedAt: Long = 0L,
)

/** Best-effort quantization label parsed from the file name (e.g. "Q4_K_M"). */
val ModelInfo.quantLabel: String?
    get() = Regex("""(?i)Q\d+(_K_[SML]|_0)?""").find(fileName)?.value?.uppercase()
