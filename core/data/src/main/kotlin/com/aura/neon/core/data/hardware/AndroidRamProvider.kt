package com.aura.neon.core.data.hardware

import android.app.ActivityManager
import android.content.Context
import com.aura.neon.core.domain.hardware.RamProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** ActivityManager-backed RAM probe (spec: PLACEMENTS - RAM probe lives in core:data). */
@Singleton
class AndroidRamProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : RamProvider {

    private val activityManager: ActivityManager?
        get() = context.getSystemService(ActivityManager::class.java)

    override fun totalRamBytes(): Long = memoryInfo().totalMem

    override fun availableRamBytes(): Long = memoryInfo().availMem

    private fun memoryInfo(): ActivityManager.MemoryInfo =
        ActivityManager.MemoryInfo().also { info -> activityManager?.getMemoryInfo(info) }
}
