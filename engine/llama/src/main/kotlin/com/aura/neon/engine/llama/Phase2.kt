package com.aura.neon.engine.llama

/**
 * PHASE 1 SHELL - intentionally empty of implementation code.
 *
 * Phase 2 adds here (see docs/SPEC.md):
 *  - the llama.cpp git submodule (third_party/llama.cpp, pinned tag b12651)
 *  - the JNI bridge + CMake build (R9 flags: LLAMA_CURL=OFF, GGML_OPENMP=OFF,
 *    GGML_VULKAN=ON, GGML_CPU_ALL_VARIANTS=ON, BUILD_SHARED_LIBS=ON)
 *  - the :engine AIDL Service (android:process=":engine", exported=false)
 *  - fd -> /proc/self/fd/N mmap loading, KV reuse, signal-safe crash handler (R12)
 *  - Binder: engine NEVER touches core:data (no DB access in this process).
 */
