package com.aura.neon.feature.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.aura.neon.core.ui.components.AuraEmptyState

/**
 * Phase 1 shell. Phase 2 wires the engine: AIDL streaming (batched per R16),
 * stop/regenerate, tokens/sec, multiple chats (spec Phase 2).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.chat_title)) })
        },
    ) { padding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AuraEmptyState(
                title = stringResource(R.string.chat_phase2_title),
                subtitle = stringResource(R.string.chat_phase2_subtitle),
            )
        }
    }
}
