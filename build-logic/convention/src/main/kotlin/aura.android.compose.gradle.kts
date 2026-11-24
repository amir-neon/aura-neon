// aura.android.compose (applied ON TOP of aura.android.application or aura.android.library)
// Spec R3: compose-compiler plugin (= Kotlin pin) + buildFeatures.compose=true
// (set in the application/library conventions - a precompiled script only gets
// typed accessors for plugins applied in ITS OWN plugins block) + platform(BOM)
// (also added in the application/library conventions - same accessor rule).
// NEVER set composeOptions.kotlinCompilerExtensionVersion.
plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}
