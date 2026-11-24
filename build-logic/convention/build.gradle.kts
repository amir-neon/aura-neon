// Convention plugins project.
// NOTE: compile classpath artifacts must stay in sync with gradle/libs.versions.toml
// (they are the same pins; the catalog is the single source of truth for versions).
plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}
