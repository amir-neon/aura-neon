package com.aura.neon.core.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.auraDataStore by preferencesDataStore(name = "aura_settings")

/**
 * Non-sensitive preferences (folder URI, onboarding flag) in plain DataStore.
 * Sensitive settings get the manual AndroidKeyStore AES-GCM treatment (R10) in Phase 4.
 */
@Singleton
class AuraSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val SELECTED_FOLDER = stringPreferencesKey("selected_folder")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    }

    val selectedFolder: Flow<String?> = context.auraDataStore.data.map { it[Keys.SELECTED_FOLDER] }
    val onboardingDone: Flow<Boolean> = context.auraDataStore.data.map { it[Keys.ONBOARDING_DONE] ?: false }

    suspend fun setSelectedFolder(uri: String?) {
        context.auraDataStore.edit { prefs ->
            if (uri == null) prefs.remove(Keys.SELECTED_FOLDER) else prefs[Keys.SELECTED_FOLDER] = uri
        }
    }

    suspend fun setOnboardingDone() {
        context.auraDataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }
}
