plugins {
    id("aura.android.library")
    id("aura.hilt")
}

// PHASE 1 SHELL: no native code yet. Phase 2 adds (spec R9):
//   - externalNativeBuild (CMake) with:
//       -DLLAMA_CURL=OFF -DGGML_OPENMP=OFF -DGGML_VULKAN=ON
//       -DGGML_CPU_ALL_VARIANTS=ON (3 target arm64 variants)
//   - the llama.cpp git submodule at third_party/llama.cpp (tag b12651)
//   - the :engine AIDL Service implementation (manifest with android:process=":engine")
dependencies {
    api(project(":engine:api"))
    implementation(project(":runtime:sandbox"))
    implementation(project(":core:common"))
}
