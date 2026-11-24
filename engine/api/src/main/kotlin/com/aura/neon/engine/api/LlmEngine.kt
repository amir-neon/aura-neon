package com.aura.neon.engine.api

/**
 * Engine API boundary (Phase 2 preview).
 *
 * The :engine AIDL service (implemented in engine:llama with
 * android:process=":engine") will expose model load / prompt / streaming-stop
 * operations behind this module's types; runtime:sandbox owns the Binder
 * client with linkToDeath handling (spec R11).
 *
 * Token streaming follows R16: the engine flushes batched tokens
 * (>= 8 tokens or 50 ms) in ONE oneway callback per flush; the client wraps
 * them into a callbackFlow.
 */
public interface LlmEngine {
    /** Backend actually selected for the current load (CPU dispatch variant or Vulkan, R9). */
    public val backend: String
}
