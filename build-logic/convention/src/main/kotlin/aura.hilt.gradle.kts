import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

// aura.hilt - Hilt + KSP for modules that use DI (spec R5)
// Room's KSP processor is added per-module (ksp room-compiler), never kapt anywhere.
//
// hilt is applied ONLY when an android base plugin is already present:
// precompiled-script accessor generation executes this script on a bare
// synthetic project (no android plugin), where hilt would fail to apply.
// Real aura modules always apply their android convention FIRST.
plugins {
    id("com.google.devtools.ksp")
}

val libsCatalog: VersionCatalog? =
    runCatching { extensions.getByType(VersionCatalogsExtension::class.java).named("libs") }.getOrNull()

val hasAndroidBase = pluginManager.hasPlugin("com.android.base") ||
    pluginManager.hasPlugin("com.android.application") ||
    pluginManager.hasPlugin("com.android.library")

if (hasAndroidBase) {
    pluginManager.apply("com.google.dagger.hilt.android")
    dependencies {
        libsCatalog?.findLibrary("hilt-android")?.get()?.let { add("implementation", it) }
        libsCatalog?.findLibrary("hilt-compiler")?.get()?.let { add("ksp", it) }
    }
} else {
    logger.lifecycle("aura.hilt: no android base plugin present (synthetic capture project or wrong order); hilt skipped")
}
