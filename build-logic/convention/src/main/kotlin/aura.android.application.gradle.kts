import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// aura.android.application
// Spec R2: EXACTLY ONE Kotlin mechanism - AGP 9 BUILT-IN Kotlin.
// Confirmed by CI (run 45316588951): AGP 9.5.2 REJECTS the external
// org.jetbrains.kotlin.android plugin ("no longer required since AGP 9.0").
// The KGP version on the build classpath stays 2.3.21 via the
// kotlin-jvm / kotlin-compose plugin markers (>= AGP's 2.2.10 minimum).
plugins {
    id("com.android.application")
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

    // R3: compose feature flag lives with the base android extensions.
    // (Non-compose modules simply ignore it.)
    buildFeatures {
        compose = true
    }
}

// Catalog access without type-safe accessors (not generated for precompiled
// scripts of an included build in this Gradle version).
val libsCatalog: org.gradle.api.artifacts.VersionCatalog? =
    runCatching { extensions.getByType(VersionCatalogsExtension::class.java).named("libs") }.getOrNull()

dependencies {
    libsCatalog?.findLibrary("compose-bom")?.get()?.let { bom ->
        add("implementation", platform(bom))
    }
}

// Built-in Kotlin: configure via the compile tasks (extension type is KGP's,
// which AGP 9 depends on at runtime - same classes, version from classpath).
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
