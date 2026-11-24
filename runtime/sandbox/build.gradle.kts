plugins {
    id("aura.android.library")
}

// PHASE 1 SHELL. Phase 2 adds the engine AIDL interfaces + the :engine client
// (linkToDeath handling per spec R11 lives in this module's client code).
dependencies {
    api(project(":engine:api"))
    implementation(project(":core:common"))
}
