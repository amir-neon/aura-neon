package com.aura.neon.feature.models

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aura.neon.core.domain.model.ModelInfo
import com.aura.neon.core.domain.model.quantLabel
import com.aura.neon.core.domain.repository.ScanError
import com.aura.neon.core.ui.components.AuraEmptyState
import com.aura.neon.core.ui.components.AuraIcons
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelsScreen(
    viewModel: ModelsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val folderPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree(),
    ) { uri ->
        if (uri != null) {
            // Persistable grant (spec R13): re-checked at scan time by the repository.
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
                )
            }
            viewModel.onFolderPicked(uri)
        }
    }

    LaunchedEffect(state.message) {
        val msg = state.message ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            when (msg) {
                is ModelsViewModel.Message.ScanDone -> context.getString(
                    R.string.scan_found, msg.found, msg.added, msg.updated,
                )
                is ModelsViewModel.Message.ScanFailed -> when (msg.reason) {
                    ScanError.NO_FOLDER -> context.getString(R.string.err_no_folder)
                    ScanError.NON_LOCAL_PROVIDER -> context.getString(R.string.err_non_local)
                    ScanError.PERMISSION_LOST -> context.getString(R.string.err_permission)
                    ScanError.EMPTY -> context.getString(R.string.err_empty)
                }
            }
        )
        viewModel.clearMessage()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.models_title)) },
                actions = {
                    IconButton(onClick = viewModel::scan, enabled = !state.scanning) {
                        Icon(AuraIcons.Search, contentDescription = stringResource(R.string.scan))
                    }
                },
            )
        },
        floatingActionButton = {
            Button(onClick = { folderPicker.launch(null) }) {
                Text(stringResource(R.string.pick_folder))
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            state.folder?.let { folder ->
                Text(
                    text = folder,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
            if (state.scanning) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
            if (state.models.isEmpty()) {
                AuraEmptyState(
                    title = stringResource(R.string.empty_models_title),
                    subtitle = stringResource(R.string.empty_models_subtitle),
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp,
                    ),
                ) {
                    items(state.models, key = { it.model.id }) { item ->
                        ModelCard(
                            model = item.model,
                            verdict = item.verdict,
                            onToggleEnabled = viewModel::toggleEnabled,
                            onTogglePin = viewModel::togglePin,
                            onSaveNote = viewModel::saveNote,
                            onRemove = viewModel::remove,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModelCard(
    model: ModelInfo,
    verdict: com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict,
    onToggleEnabled: (ModelInfo) -> Unit,
    onTogglePin: (ModelInfo) -> Unit,
    onSaveNote: (ModelInfo, String) -> Unit,
    onRemove: (ModelInfo) -> Unit,
) {
    var showNoteDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.fileName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = formatFileSize(model.sizeBytes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                model.quantLabel?.let { quant ->
                    VerdictChip(
                        container = MaterialTheme.colorScheme.surfaceVariant,
                        content = MaterialTheme.colorScheme.onSurfaceVariant,
                        label = quant,
                    )
                }
                VerdictChip(
                    container = when (verdict) {
                        com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.OK ->
                            MaterialTheme.colorScheme.tertiaryContainer
                        com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.TIGHT ->
                            MaterialTheme.colorScheme.secondaryContainer
                        com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.TOO_LARGE ->
                            MaterialTheme.colorScheme.errorContainer
                    },
                    content = when (verdict) {
                        com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.OK ->
                            MaterialTheme.colorScheme.onTertiaryContainer
                        com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.TIGHT ->
                            MaterialTheme.colorScheme.onSecondaryContainer
                        com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.TOO_LARGE ->
                            MaterialTheme.colorScheme.onErrorContainer
                    },
                    label = stringResource(
                        when (verdict) {
                            com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.OK -> R.string.verdict_ok
                            com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.TIGHT -> R.string.verdict_tight
                            com.aura.neon.core.domain.estimate.ResourceEstimator.Verdict.TOO_LARGE -> R.string.verdict_too_large
                        }
                    ),
                )
            }

            if (model.note.isNotBlank()) {
                Text(
                    text = model.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.enabled),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = model.enabled,
                    onCheckedChange = { onToggleEnabled(model) },
                )
                IconButton(onClick = { onTogglePin(model) }) {
                    Icon(
                        AuraIcons.Pin,
                        contentDescription = stringResource(if (model.pinned) R.string.unpin else R.string.pin),
                        tint = if (model.pinned) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { showNoteDialog = true }) {
                    Icon(AuraIcons.Sliders, contentDescription = stringResource(R.string.edit_note))
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(AuraIcons.Trash, contentDescription = stringResource(R.string.delete))
                }
            }
        }
    }

    if (showNoteDialog) {
        NoteDialog(
            initial = model.note,
            onDismiss = { showNoteDialog = false },
            onConfirm = { text ->
                showNoteDialog = false
                onSaveNote(model, text)
            },
        )
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_confirm_title)) },
            text = { Text(stringResource(R.string.delete_confirm_text)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onRemove(model)
                }) { Text(stringResource(R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun NoteDialog(
    initial: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_note)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(stringResource(R.string.note_hint)) },
                singleLine = false,
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    )
}

@Composable
private fun VerdictChip(container: androidx.compose.ui.graphics.Color, content: androidx.compose.ui.graphics.Color, label: String) {
    Surface(color = container, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = content,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

private fun formatFileSize(bytes: Long): String {
    if (bytes < 0) return "?"
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1.0 -> String.format(Locale.US, "%.1f GB", gb)
        mb >= 1.0 -> String.format(Locale.US, "%.0f MB", mb)
        kb >= 1.0 -> String.format(Locale.US, "%.0f KB", kb)
        else -> "$bytes B"
    }
}
