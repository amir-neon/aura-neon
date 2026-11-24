import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// aura.jvm.library - pure Kotlin JVM modules (core:domain, engine:api)
plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
