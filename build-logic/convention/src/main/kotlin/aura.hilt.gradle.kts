// aura.hilt - Hilt + KSP for modules that use DI (spec R5)
// Room's KSP processor is added per-module (ksp room-compiler), never kapt anywhere.
//
// NOTE: hilt is applied ONLY when an android base plugin is already present.
// Precompiled-script accessor generation executes this script on a bare
// synthetic project (no android plugin) - applying hilt there would fail.
// Real aura modules always apply their android convention FIRST.
plugins {
    id("com.google.devtools.ksp")
}

val hasAndroidBase = pluginManager.hasPlugin("com.android.base") ||
    pluginManager.hasPlugin("com.android.application") ||
    pluginManager.hasPlugin("com.android.library")

if (hasAndroidBase) {
    pluginManager.apply("com.google.dagger.hilt.android")
    dependencies {
        add("implementation", libs.hilt.android)
        add("ksp", libs.hilt.compiler)
    }
} else {
    logger.lifecycle("aura.hilt: no android base plugin present (synthetic capture project or wrong order); hilt skipped")
}
