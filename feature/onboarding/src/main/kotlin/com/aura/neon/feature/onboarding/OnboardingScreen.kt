package com.aura.neon.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * First-run guide (spec Phase 5 lands the full version; this covers the
 * offline philosophy + folder + GitHub install basics needed in Phase 1).
 */
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = listOf(
        R.string.onboarding_title_1 to R.string.onboarding_body_1,
        R.string.onboarding_title_2 to R.string.onboarding_body_2,
        R.string.onboarding_title_3 to R.string.onboarding_body_3,
    )
    var page by remember { mutableIntStateOf(0) }
    val isLast = page == pages.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.onboarding_welcome),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(pages[page].first),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(pages[page].second),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (page > 0) {
                OutlinedButton(onClick = { page-- }) {
                    Text(stringResource(R.string.onboarding_back))
                }
            }
            Button(onClick = { if (isLast) onFinished() else page++ }) {
                Text(
                    text = stringResource(
                        if (isLast) R.string.onboarding_start else R.string.onboarding_next,
                    ),
                )
            }
        }
    }
}
