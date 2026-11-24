package com.aura.neon.feature.settings

import androidx.lifecycle.ViewModel
import com.aura.neon.core.domain.hardware.RamProvider
import com.aura.neon.core.domain.model.DeviceProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    ram: RamProvider,
) : ViewModel() {
    val deviceProfile: DeviceProfile = DeviceProfile.fromTotalRam(ram.totalRamBytes())
    val totalRamBytes: Long = ram.totalRamBytes()
}
