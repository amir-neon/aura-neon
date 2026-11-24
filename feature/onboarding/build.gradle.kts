plugins {
    id("aura.android.library")
    id("aura.android.compose")
}

android {
    buildFeatures {
        compose = true
    }
}

// No Hilt here (spec module table). The onboarding-done flag is written by :app
// through core:data; this module is pure UI.
dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
}
