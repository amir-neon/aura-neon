package com.aura.neon.core.domain.estimate

/**
 * Resource estimator (spec R15).
 *
 * KV budget = 2 * n_layers * n_ctx * n_kv_heads * head_dim * bytes_per_elem
 *
 * bytes_per_elem constants (spec v2.5 / F13):
 *   f16 = 2.0
 *   q8_0 = 1.0625   (32 values per block: 32 bytes weights + 2 bytes scale)
 *   q4_0 = 0.5625   (32 values per block: 16 bytes weights + 2 bytes scale)
 *
 * Phase 1 honesty note: GGUF header fields (n_layers, kv heads, head_dim) are
 * only read from the file starting in Phase 2. Until then [estimate] uses
 * documented default arch values with the file size as weights; the verdict
 * is a coarse-but-honest warning, refined per-architecture in Phase 2.
 */
object ResourceEstimator {

    const val KV_BYTES_F16: Double = 2.0
    const val KV_BYTES_Q8_0: Double = 1.0625
    const val KV_BYTES_Q4_0: Double = 0.5625

    // Documented defaults for the estimate-only path (Phase 1):
    // a mid-size 7B-class arch: 32 layers, 32 kv heads, 128 head dim, ctx 2048.
    private const val DEFAULT_N_LAYERS = 32
    private const val DEFAULT_N_KV_HEADS = 32
    private const val DEFAULT_HEAD_DIM = 128
    private const val DEFAULT_N_CTX = 2048

    fun kvCacheBytes(
        nLayers: Int = DEFAULT_N_LAYERS,
        nCtx: Int = DEFAULT_N_CTX,
        nKvHeads: Int = DEFAULT_N_KV_HEADS,
        headDim: Int = DEFAULT_HEAD_DIM,
        bytesPerElem: Double = KV_BYTES_F16,
    ): Long {
        val elements = 2.0 * nLayers * nCtx * nKvHeads * headDim
        return (elements * bytesPerElem).toLong()
    }

    enum class Verdict { OK, TIGHT, TOO_LARGE }

    data class Estimate(val weightsBytes: Long, val kvBytes: Long, val verdict: Verdict)

    /**
     * Coarse Phase-1 estimate: weights = file size, KV = default arch budget.
     * Verdict thresholds against AVAILABLE RAM (not total): weights+KV must
     * fit with headroom; >80% of available = TIGHT, >100% = TOO_LARGE.
     */
    fun estimate(weightsBytes: Long, availableRamBytes: Long): Estimate {
        val kv = kvCacheBytes()
        val total = weightsBytes + kv
        val verdict = when {
            total <= availableRamBytes * 8 / 10 -> Verdict.OK
            total <= availableRamBytes -> Verdict.TIGHT
            else -> Verdict.TOO_LARGE
        }
        return Estimate(weightsBytes, kv, verdict)
    }
}
