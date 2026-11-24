package com.aura.neon.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.aura.neon.core.ui.R

/**
 * Single font: Vazirmatn (OFL). One variable font file covers fa + en scripts
 * across weights (spec: SINGLE font policy).
 */
val Vazirmatn: FontFamily = FontFamily(
    Font(R.font.vazirmatn, weight = FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.vazirmatn, weight = FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.vazirmatn, weight = FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.vazirmatn, weight = FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
)

private val base = Typography()

val AuraTypography: Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = Vazirmatn),
    displayMedium = base.displayMedium.copy(fontFamily = Vazirmatn),
    displaySmall = base.displaySmall.copy(fontFamily = Vazirmatn),
    headlineLarge = base.headlineLarge.copy(fontFamily = Vazirmatn),
    headlineMedium = base.headlineMedium.copy(fontFamily = Vazirmatn),
    headlineSmall = base.headlineSmall.copy(fontFamily = Vazirmatn),
    titleLarge = base.titleLarge.copy(fontFamily = Vazirmatn),
    titleMedium = base.titleMedium.copy(fontFamily = Vazirmatn),
    titleSmall = base.titleSmall.copy(fontFamily = Vazirmatn),
    bodyLarge = base.bodyLarge.copy(fontFamily = Vazirmatn),
    bodyMedium = base.bodyMedium.copy(fontFamily = Vazirmatn),
    bodySmall = base.bodySmall.copy(fontFamily = Vazirmatn),
    labelLarge = base.labelLarge.copy(fontFamily = Vazirmatn),
    labelMedium = base.labelMedium.copy(fontFamily = Vazirmatn),
    labelSmall = base.labelSmall.copy(fontFamily = Vazirmatn),
)
