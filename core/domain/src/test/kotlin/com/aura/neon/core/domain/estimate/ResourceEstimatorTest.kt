package com.aura.neon.core.domain.estimate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceEstimatorTest {

    @Test
    fun `kv cache math matches the R15 formula`() {
        // KV = 2 * n_layers * n_ctx * n_kv_heads * head_dim * bytes_per_elem
        // = 2 * 8 * 512 * 4 * 64 = 2_097_152 elements; f16 (2.0 B/elem) -> 4_194_304 bytes
        assertEquals(4_194_304L, ResourceEstimator.kvCacheBytes(nLayers = 8, nCtx = 512, nKvHeads = 4, headDim = 64))
        // q8_0 (1.0625 B/elem) for the same arch:
        assertEquals(2_228_224L, ResourceEstimator.kvCacheBytes(nLayers = 8, nCtx = 512, nKvHeads = 4, headDim = 64, bytesPerElem = ResourceEstimator.KV_BYTES_Q8_0))
    }

    @Test
    fun `quantized kv constants carry block overhead`() {
        // q8_0: 32 elems per 34-byte block -> 34/32 = 1.0625 bytes per element
        assertEquals(1.0625, ResourceEstimator.KV_BYTES_Q8_0, 1e-12)
        // q4_0: 32 elems per 18-byte block -> 18/32 = 0.5625 bytes per element
        assertEquals(0.5625, ResourceEstimator.KV_BYTES_Q4_0, 1e-12)
    }

    @Test
    fun `kv shrinks when quantized`() {
        val f16 = ResourceEstimator.kvCacheBytes(bytesPerElem = ResourceEstimator.KV_BYTES_F16)
        val q4 = ResourceEstimator.kvCacheBytes(bytesPerElem = ResourceEstimator.KV_BYTES_Q4_0)
        assertTrue(q4 < f16 / 3)
    }

    @Test
    fun `verdicts split against available ram`() {
        val available = 1_000_000_000L
        // weights=0 but default KV (1 GiB) vs 1 GB available -> TOO_LARGE
        val tightCase = ResourceEstimator.estimate(weightsBytes = 0, availableRamBytes = available)
        assertEquals(ResourceEstimator.Verdict.TOO_LARGE, tightCase.verdict)

        // 1 GiB KV vs 4 GB available -> well under 80% -> OK
        val okCase = ResourceEstimator.estimate(weightsBytes = 0, availableRamBytes = available * 4)
        assertEquals(ResourceEstimator.Verdict.OK, okCase.verdict)
    }
}
