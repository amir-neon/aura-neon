import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// aura.jvm.library - pure Kotlin JVM modules (core:domain, engine:api)
plugins {
    id("org.jetbrains.kotlin.jvm")
}

// R7: JDK 21 runs Gradle, but all compiled targets are 17 (Java AND Kotlin).
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
