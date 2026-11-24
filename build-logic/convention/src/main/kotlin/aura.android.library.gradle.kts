import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// aura.android.library
// Spec R6: unique namespace com.aura.neon.<module.path>, computed from the project path.
// Spec R2: AGP 9 built-in Kotlin only (external kotlin-android is rejected by AGP 9.5.2).
plugins {
    id("com.android.library")
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

val libsCatalog: VersionCatalog? =
    runCatching { extensions.getByType(VersionCatalogsExtension::class.java).named("libs") }.getOrNull()

dependencies {
    libsCatalog?.findLibrary("compose-bom")?.get()?.let { bom ->
        add("implementation", platform(bom))
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
