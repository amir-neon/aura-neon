import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// aura.android.application
// Spec R2: EXACTLY ONE Kotlin mechanism per Android module (external
// org.jetbrains.kotlin.android here), defined ONCE in build-logic. Never also AGP built-in.
// Spec R6: app namespace = com.aura.neon; applicationId = com.aura.neon.
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.aura.neon"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aura.neon"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
