// aura.hilt - Hilt + KSP for modules that use DI (spec R5)
// Room's KSP processor is added per-module (ksp room-compiler), never kapt anywhere.
plugins {
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(libs.hilt.android)
    add("ksp", libs.hilt.compiler)
}
