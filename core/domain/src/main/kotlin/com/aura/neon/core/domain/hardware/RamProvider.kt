package com.aura.neon.core.domain.hardware

/**
 * RAM probe contract. Android implementation (ActivityManager.MemoryInfo)
 * lives in core:data (spec: PLACEMENTS).
 */
interface RamProvider {
    fun totalRamBytes(): Long
    fun availableRamBytes(): Long
}
