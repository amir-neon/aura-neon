package com.aura.neon.feature.models

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.neon.core.domain.hardware.RamProvider
import com.aura.neon.core.domain.estimate.ResourceEstimator
import com.aura.neon.core.domain.model.DeviceProfile
import com.aura.neon.core.domain.model.ModelInfo
import com.aura.neon.core.domain.repository.ModelRepository
import com.aura.neon.core.domain.repository.ScanError
import com.aura.neon.core.domain.repository.ScanOutcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelsViewModel @Inject constructor(
    private val repository: ModelRepository,
    private val ram: RamProvider,
) : ViewModel() {

    data class ModelUi(val model: ModelInfo, val verdict: ResourceEstimator.Verdict)

    sealed interface Message {
        data class ScanDone(val found: Int, val added: Int, val updated: Int) : Message
        data class ScanFailed(val reason: ScanError) : Message
    }

    data class UiState(
        val folder: String? = null,
        val models: List<ModelUi> = emptyList(),
        val deviceProfile: DeviceProfile = DeviceProfile.ECO,
        val scanning: Boolean = false,
        val message: Message? = null,
    )

    private val busy = MutableStateFlow(false)
    private val message = MutableStateFlow<Message?>(null)

    val state: StateFlow<UiState> =
        combine(repository.models, repository.selectedFolder, busy, message) { models, folder, isBusy, msg ->
            val availableRam = ram.availableRamBytes()
            UiState(
                folder = folder,
                models = models.map { info ->
                    ModelUi(info, ResourceEstimator.estimate(info.sizeBytes, availableRam).verdict)
                },
                deviceProfile = DeviceProfile.fromTotalRam(ram.totalRamBytes()),
                scanning = isBusy,
                message = msg,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun clearMessage() {
        message.value = null
    }

    /** Called after the UI layer took the persistable URI permission (spec R13). */
    fun onFolderPicked(uri: Uri) {
        viewModelScope.launch {
            repository.setSelectedFolder(uri.toString())
            scan()
        }
    }

    fun scan() {
        viewModelScope.launch {
            busy.value = true
            message.value = when (val outcome: ScanOutcome = repository.scan()) {
                is ScanOutcome.Success ->
                    Message.ScanDone(outcome.found, outcome.added, outcome.updated)
                is ScanOutcome.Failure ->
                    Message.ScanFailed(outcome.reason)
            }
            busy.value = false
        }
    }

    fun toggleEnabled(model: ModelInfo) {
        viewModelScope.launch { repository.setEnabled(model.id, !model.enabled) }
    }

    fun togglePin(model: ModelInfo) {
        viewModelScope.launch { repository.setPinned(model.id, !model.pinned) }
    }

    fun saveNote(model: ModelInfo, note: String) {
        viewModelScope.launch { repository.setNote(model.id, note.trim()) }
    }

    /** Unregister ONLY - the file on disk is never touched (spec). */
    fun remove(model: ModelInfo) {
        viewModelScope.launch { repository.remove(model.id) }
    }
}
