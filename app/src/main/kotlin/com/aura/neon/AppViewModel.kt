package com.aura.neon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.neon.core.data.settings.AuraSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** App-shell state: the onboarding gate. Feature screens own their own ViewModels. */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val settings: AuraSettings,
) : ViewModel() {

    val onboardingDone: StateFlow<Boolean?> = settings.onboardingDone
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun completeOnboarding() {
        viewModelScope.launch { settings.setOnboardingDone() }
    }
}
