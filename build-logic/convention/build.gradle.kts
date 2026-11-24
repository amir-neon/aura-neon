// Convention plugins project.
// The plugin implementations below must be on the compile classpath so that
// precompiled script plugins can resolve them BY ID (no version in scripts).
// Versions come from the shared catalog (single source of truth).
plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kotlin.compose.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}
