plugins {
    id("aura.android.application")
    id("aura.android.compose")
    id("aura.hilt")
}

android {
    // R3: compose feature flag (module-local: AGP requires the compose plugin
    // wherever this flag is on; non-compose modules keep it off)
    buildFeatures {
        compose = true
    }

    // R17 (v2.5): minify OFF is a documented decision, not an omission.
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    implementation(project(":feature:chat"))
    implementation(project(":feature:models"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:onboarding"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":engine:api"))
    implementation(project(":engine:llama"))
    implementation(project(":runtime:sandbox"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
}
