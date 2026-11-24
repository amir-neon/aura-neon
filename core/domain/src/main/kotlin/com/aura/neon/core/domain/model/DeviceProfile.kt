package com.aura.neon.core.domain.model

/**
 * Device performance profiles (spec: DEVICE PROFILES section).
 * Pure data, lives in core:domain (spec: PLACEMENTS).
 */
enum class DeviceProfile(
    val label: String,
    val ramRangeGib: ClosedFloatingPointRange<Double>,
    val threads: IntRange,
    val context: IntRange,
    val gpuLayers: Boolean,
    val kvQuant: String,
    val maxTokens: Int,
) {
    ECO(
        label = "eco",
        ramRangeGib = 2.0..4.0,
        threads = 2..4,
        context = 512..1024,
        gpuLayers = false,
        kvQuant = "q4_0 (FA)",
        maxTokens = 256,
    ),
    BALANCED(
        label = "balanced",
        ramRangeGib = 4.0..6.0,
        threads = 4..6,
        context = 2048..2048,
        gpuLayers = false, // optional in spec; decided per device in Phase 2
        kvQuant = "q8_0 (FA)",
        maxTokens = 512,
    ),
    PERFORMANCE(
        label = "performance",
        ramRangeGib = 8.0..Double.MAX_VALUE,
        threads = 6..16,
        context = 4096..32768,
        gpuLayers = true,
        kvQuant = "f16",
        maxTokens = 2048,
    );

    companion object {
        private const val GIB: Double = 1024.0 * 1024.0 * 1024.0

        fun fromTotalRam(ramBytes: Long): DeviceProfile {
            val gib = ramBytes / GIB
            return entries.firstOrNull { gib in it.ramRangeGib }
                ?: if (gib >= 6.0) PERFORMANCE else ECO
        }
    }
}
