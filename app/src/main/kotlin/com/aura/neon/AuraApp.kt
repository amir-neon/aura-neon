package com.aura.neon

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aura.neon.core.ui.components.AuraIcons
import com.aura.neon.feature.chat.ChatScreen
import com.aura.neon.feature.models.ModelsScreen
import com.aura.neon.feature.settings.SettingsScreen
import androidx.compose.material3.Icon

private object Routes {
    const val CHAT = "chat"
    const val MODELS = "models"
    const val SETTINGS = "settings"
}

@Composable
fun AuraApp(viewModel: AppViewModel = hiltViewModel()) {
    val onboarded by viewModel.onboardingDone.collectAsStateWithLifecycle()
    when (onboarded) {
        null -> Unit // DataStore read in flight: theme background shows meanwhile.
        false -> OnboardingGate(onFinished = viewModel::completeOnboarding)
        true -> AuraNavHost()
    }
}

@Composable
private fun OnboardingGate(onFinished: () -> Unit) {
    com.aura.neon.feature.onboarding.OnboardingScreen(onFinished = onFinished)
}

@Composable
private fun AuraNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Routes.CHAT,
                    onClick = { navController.navigate(Routes.CHAT) { launchSingleTop = true } },
                    icon = { Icon(AuraIcons.Chat, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_chat)) },
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.MODELS,
                    onClick = { navController.navigate(Routes.MODELS) { launchSingleTop = true } },
                    icon = { Icon(AuraIcons.Grid, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_models)) },
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.SETTINGS,
                    onClick = { navController.navigate(Routes.SETTINGS) { launchSingleTop = true } },
                    icon = { Icon(AuraIcons.Sliders, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_settings)) },
                )
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.CHAT,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.CHAT) { ChatScreen() }
            composable(Routes.MODELS) { ModelsScreen() }
            composable(Routes.SETTINGS) { SettingsScreen() }
        }
    }
}
