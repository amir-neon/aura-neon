// Convention plugins project.
// The plugin implementations below must be on the classpath so that precompiled
// script plugins can (a) resolve them BY ID at script compile time and
// (b) actually FIND them at apply time on the target modules.
// compileOnly is NOT enough: the plugin classes must travel with the
// convention plugin at runtime (hence implementation).
// Versions come from the shared catalog (single source of truth).
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
}
