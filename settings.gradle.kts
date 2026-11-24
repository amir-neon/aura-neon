pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "aura-neon"

include(
    ":app",
    ":core:common",
    ":core:domain",
    ":core:data",
    ":core:ui",
    ":engine:api",
    ":engine:llama",
    ":runtime:sandbox",
    ":feature:chat",
    ":feature:models",
    ":feature:settings",
    ":feature:onboarding",
)
