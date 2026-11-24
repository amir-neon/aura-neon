// aura.android.compose (applied ON TOP of aura.android.application or aura.android.library)
// Spec R3: compose-compiler plugin (= Kotlin pin) + buildFeatures.compose=true + platform(BOM).
// NEVER set composeOptions.kotlinCompilerExtensionVersion here.
plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
}
