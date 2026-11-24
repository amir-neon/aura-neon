import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// aura.android.library
// Spec R6: unique namespace com.aura.neon.<module.path>, computed from the project path.
// Spec R2: external org.jetbrains.kotlin.android (ONE mechanism, defined here only).
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val auraNamespace: String = "com.aura.neon" + project.path.replace(":", ".")

android {
    namespace = auraNamespace
    compileSdk = 36

    defaultConfig {
        minSdk = 29
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
