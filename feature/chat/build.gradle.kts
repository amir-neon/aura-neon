plugins {
    id("aura.android.library")
    id("aura.android.compose")
    id("aura.hilt")
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":engine:api"))
    implementation(project(":runtime:sandbox"))

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
