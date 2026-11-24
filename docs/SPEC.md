============================================================
AURA NEON - PROJECT SPECIFICATION v2.6 (REVISED + WEB-VERIFIED)
Last updated: 2026-11-24
Package: com.aura.neon
Status: STRUCTURE LOCKED - pins resolved live from Maven/Google
        (24/24 preflight probes) on 2026-11-24; CI re-verifies forever
Distribution: GitHub Releases only (Google Play: NEVER)
============================================================

CHANGE LOG
------------------------------------------------------------
v2.4 -> v2.5 (logic fixes):
  F1  Guards 1/2/3 exit-code inversion fixed -> fail-if-found blocks
  F3  R9 missing -DGGML_CPU_ALL_VARIANTS=ON (required by Phase 2 dispatch)
  F4  submodule-bump contradiction fixed -> bot branch + PR, never push main
  F5  unbacked "0 crashes" metric -> manual Release QA Checklist
  F6-F17 consistency: Phase 3 checkboxes, R16 token batching, R17 minify
      decision, Robolectric pin, release.yml build-tools, NDK deferred to
      Phase 2 in ci.yml, KV bytes/elem constants, PLACEMENTS, keystore
      retention-days, Renovate regex, header typo
v2.5 -> v2.6 (live verification pass, 2026-11-24):
  W1  Kotlin 2.3.14 DOES NOT EXIST (checked kotlin-stdlib maven-metadata
      + direct POM 404). Corrected to 2.3.21 (latest of the 2.3.x line;
      latest stable overall is 2.4.21 - user decision to jump, default kept).
  W2  KSP 2.3.14 IS REAL (standalone versioning, 2.3.0..2.3.14 line).
      v2.5 wrongly "fixed" it to a composite version - REVERTED.
      R4 rewritten: KSP2 standalone versioning, pairing checked by
      preflight probes + the first build as arbiter.
  W3  AGP 9.5.2 IS REAL (metadata tail + POM 200). All 14 user pins
      verified live; AGP 9.5.0/9.5.1/9.5.2 all exist.
  W4  Robolectric latest stable = 4.18.1 (new declared pin).
  W5  Preflight probe treats 429/5xx as transient (rate limits), only
      definitive 404 fails a pin.
  W6  AGP 9.5.2 REJECTS the external org.jetbrains.kotlin.android
      plugin ("no longer required since AGP 9.0", CI run 45316588951)
      -> R2 flipped to BUILT-IN Kotlin in the two Android convention
      plugins (the single flip point R2 was designed for). KGP 2.3.21
      stays on the build classpath via the kotlin-jvm/compose markers
      (AGP runtime minimum is 2.2.10 - no forced downgrade).
  W7  androidx 2026.11 line (hilt 1.4.0, compose.ui 1.13.0) requires
      compileSdk 37 / 37.1 (minor SDK releases). Spec clause applied:
      "raise further only if deps demand it" -> compileSdk = 37 +
      compileSdkMinor = 1 (targetSdk 34 unchanged, FINAL).
  W8  FIRST FULLY GREEN BUILD: run 45328264207 (2026-11-24) -
      preflight + assembleDebug + test + all 3 guards + artifact upload.

PROJECT INFO
------------------------------------------------------------
Project Name      : Aura
App Name (device) : aura (lowercase)
Package ID        : com.aura.neon
minSdk            : 29 (Android 10)
targetSdk         : 34 (Android 14) - FINAL, no Play upgrades
compileSdk        : 37 (Android 17) - 2026 androidx requires it:
                    androidx.hilt 1.4.0 AAR metadata demands 37
                    (spec clause: raise only if deps demand it - they do).
                    targetSdk stays 34 FINAL.
Primary Language  : Kotlin
Engine Core       : C++ (llama.cpp, pinned tag b12651, submodule)
UI Framework      : Jetpack Compose + Material 3
Architecture      : MVVM + Clean Architecture
Modules           : 12 app modules + build-logic (included build)
DI                : Hilt + KSP2
NDK               : r30 (30.0.16248370), pinned, 16 KB page size
Font              : Vazirmatn - single variable font, fa + en
                    (OFL file ships at docs/licenses/OFL-Vazirmatn.txt)
UI Locale         : fa default + en secondary (values-en)
License           : Apache 2.0 (code) + OFL 1.1 (font)
Distribution      : GitHub Releases only - signed APK, arm64-v8a.
                    No AAB, no Play Signing, no Play Console.
Workflow          : Phone-only. All builds run in GitHub Actions.

BUILD STACK (DECLARED PINS - ALL WEB-VERIFIED 2026-11-24)
------------------------------------------------------------
Gradle                  : 9.9.2              (dist URL reachable)
AGP                     : 9.5.2              (POM 200; 9.5.x line real)
Kotlin                  : 2.3.21             (2.3.14 does not exist)
KSP                     : 2.3.14             (standalone versioning)
Compose BOM             : 2026.11.01         (latest stable)
Compose compiler        : bundled with Kotlin (plugin = Kotlin pin)
androidx.core:core-ktx  : 1.19.1
activity-compose        : 1.13.0
lifecycle               : 2.11.1
navigation-compose      : 2.10.2
Room                    : 2.8.5
DataStore               : 1.3.0 (datastore-preferences)
Hilt                    : 2.60.1
hilt-navigation-compose : 1.4.0
kotlinx-coroutines      : 1.11.0
JUnit                   : 4.13.2
Turbine                 : 1.2.1
Robolectric             : 4.18.1 (build-time only, Room DAO JVM tests)
NDK                     : 30.0.16248370 (r30)
llama.cpp               : tag b12651 (git submodule, pinned)

VERSION POLICY (FIXED)
------------------------------------------------------------
- Latest STABLE only, pinned in Version Catalog
- No alpha / beta / rc / dev / canary / EAP / snapshot
- Renovate weekly grouped PRs:
    * androidx / compose patch+minor -> auto-merge after CI green
    * build-toolchain (AGP/Gradle/Kotlin/KSP/NDK/Hilt) -> ALWAYS manual
    * major -> manual
- llama.cpp EXCLUDED from Renovate (rolling-release "pre-release" tags);
  dedicated monthly workflow bumps the submodule to a pinned tag.

CORE PRINCIPLES (NON-NEGOTIABLE)
------------------------------------------------------------
1. Small     2. Complete    3. Beautiful (Material You)
4. Smooth (pure native) 5. Powerful (Vulkan + CPU dispatch)
6. Stable    7. Secure (sandbox, encryption, zero telemetry)
8. Offline (zero internet, enforced at binary level too)

PROHIBITED (NEVER ADD)
------------------------------------------------------------
INTERNET permission; network libs (Retrofit/OkHttp/Ktor); libcurl /
LLAMA_CURL; Firebase; Play Services/Integrity/Billing; analytics;
online crash reporting; model download; copying model files; user
registration/API keys; telemetry; in-app updater; React Native /
Flutter; non-GGUF formats; remote APIs (OpenAI/Claude/Ollama);
HuggingFace; marketplace/IAP; speech models (Whisper/Kokoro);
multiple models simultaneously; custom personas; model benchmark;
cloud backup; multiple fonts; text size slider; manual accent picker;
separate AMOLED theme; biometric lock; secure file deletion; memory
tagging extension; security-crypto (deprecated); OpenMP (ggml
threadpool only); isolatedProcess for engine (blocks Vulkan);
non-stable library versions.

MODULES + DEPENDENCY GRAPH (ACYCLIC)
------------------------------------------------------------
aura-neon/
+- build-logic/                 # included build, convention plugins
+- gradle/libs.versions.toml    # pinned catalog
+- app/                         # Application, Compose, Hilt
+- core/{common,domain,data,ui}/
+- engine/{api,llama}/          # llama/third_party/llama.cpp submodule
+- runtime/sandbox/             # AIDL + :engine client
+- feature/{chat,models,settings,onboarding}/

GRAPH:
app -> features, core:ui, core:data, core:domain, engine:api,
       engine:llama, runtime:sandbox, core:common
feature/* -> core:ui, core:domain, core:common
feature:chat -> engine:api, runtime:sandbox
core:data -> core:domain, core:common
engine:llama -> engine:api, runtime:sandbox, core:common (NEVER core:data)
runtime:sandbox -> engine:api, core:common
>> NO CYCLES. Engine has NO dependency on core:data (no DB access).
>> AIDL lives in runtime:sandbox; Service impl in engine:llama with
   android:process=":engine", android:exported="false".

PLACEMENTS (RESOLVED)
------------------------------------------------------------
- DeviceProfile presets      -> core:domain (pure)
- RAM probe (ActivityManager)-> core:data
- CPU topology/cluster detect-> engine:llama native (via engine:api)
- BackendSelector use case   -> core:domain
- UI strings                  : fa default + values-en

BUILD RULES (CRITICAL)
------------------------------------------------------------
R1  Catalog at gradle/libs.versions.toml. All versions pinned.
R2  AGP 9 Kotlin: EXACTLY ONE mechanism - BUILT-IN Kotlin. AGP 9.5.2
    rejects the external org.jetbrains.kotlin.android plugin
    (CI-confirmed, run 45316588951). Defined ONCE in build-logic;
    KGP on the classpath = the Kotlin pin via jvm/compose markers
    (AGP runtime minimum 2.2.10).
R3  Every Compose module: compose-compiler plugin (= Kotlin pin) +
    buildFeatures.compose=true + platform(BOM). NEVER set
    composeOptions.kotlinCompilerExtensionVersion.
R4  KSP2 standalone versioning does not remove pairing risk; pins
    must resolve (preflight) and the first build arbitrates the
    KSP<->Kotlin pairing. Room + Hilt processors via ksp(...) only.
R5  Every Hilt module: hilt + ksp plugins + ksp(hilt-compiler).
R6  Unique namespaces com.aura.neon.<module.path> (computed in the
    library convention from the project path; app = com.aura.neon).
R7  JDK 21 runs Gradle; jvmTarget = 17 everywhere. No jvmToolchain
    auto-provisioning. configuration-cache OFF until verified.
R8  build-logic is an included build with convention plugins:
    aura.android.application, aura.android.library,
    aura.android.compose, aura.jvm.library, aura.hilt.
    Module build files stay thin.
R9  Native: arm64-v8a only. CMake flags:
      -DLLAMA_CURL=OFF -DGGML_OPENMP=OFF -DGGML_VULKAN=ON
      -DBUILD_SHARED_LIBS=ON -DGGML_CPU_ALL_VARIANTS=ON
      (variants restricted to the 3 targets: armv8.0 baseline /
      v8.2-a+dotprod+fp16 / v8.6-a+i8mm)
    Linker: -Wl,-z,max-page-size=16384
    packaging.jniLibs.useLegacyPackaging = false
    Release must pass: zipalign -c -P 16 4 <apk>
R10 Encrypted settings = manual AndroidKeyStore AES-GCM + encrypted
    DataStore in core:data (~100 lines, zero extra deps).
R11 Binder: linkToDeath on engine service; on death -> clear error,
    stop stream, allow reconnect/restart. No silent hangs.
R12 Native crash handler: async-signal-safe only; Java handler writes
    crash.log.
R13 SAF: persistable URI; re-check permission at load/scan time;
    SecurityException -> re-prompt; reject non-local providers with a
    curated deny-list + clear error (honest heuristic); no file copy.
R14 Model identity at scan = URI + size + lastModified (no bytes read).
R15 Resource Estimator: weights(file size) + KV budget
    (2 * n_layers * n_ctx * n_kv_heads * head_dim * bytes_per_elem)
    vs available RAM. bytes/elem: f16=2.0, q8_0=1.0625, q4_0=0.5625.
    Phase 1 uses documented default arch values until GGUF metadata is
    read in Phase 2.
R16 Token streaming over AIDL MUST be batched: engine flushes when
    buffer >= 8 tokens OR 50 ms since last flush -> ONE oneway callback
    per flush; client uses callbackFlow + conflate().
R17 Release: minifyEnabled=false + shrinkResources=false (documented
    decision pre-1.0; revisit with measured diff + keep rules).

FEATURES - PHASE BY PHASE
------------------------------------------------------------
PHASE 1: Infrastructure + Model Management      [SHIPPED 2026-11-24]
[x] Multi-module Gradle + build-logic convention plugins
[x] Version Catalog (all pins web-verified) + CI preflight
[x] Renovate config (fixed policy)
[x] submodule-bump workflow (phone-only friendly)
[x] keystore-bootstrap workflow (retention 1 day)
[x] Folder selection with SAF (persistable URI, local only)
[x] Scan .gguf, register in Room (NO copy), prune vanished
[x] Reject non-local providers with clear error
[x] Model list / enable-disable / pin / notes / remove
[x] Resource estimator badge (RAM verdict per model)

PHASE 2: Engine + Basic Chat
[ ] llama.cpp submodule pinned b12651 (CI: submodules recursive)
[ ] JNI bridge
[ ] fd -> /proc/self/fd/N in :engine (zero copy, mmap)
[ ] Auto chat template from GGUF metadata at load
[ ] CPU: all cores prompt, big cores generation
[ ] Vulkan opt-in + automatic CPU fallback
[ ] Runtime CPU dispatch - 3 variants (R9 flags)
[ ] Incremental prompt processing (KV reuse)
[ ] Token streaming: AIDL oneway batched (R16) -> callbackFlow
[ ] Stop generation / multiple chats / rename-delete / tokens/sec

PHASE 3: Complete Model Settings
[ ] Loading: Context(256-32768), Batch, Threads, GPU Layers, Main
    GPU, Tensor Split, MMAP, MLock, Flash Attention, KV type
    (f16/q8_0/q4_0; quantized KV auto-enables FA)
[ ] Sampling: Temp, TopK, TopP, MinP, TypicalP, RepeatPenalty,
    RepeatLastN, FreqPenalty, PresPenalty, Mirostat, Seed, MaxTokens
[ ] Template: Auto(GGUF)=DEFAULT; overrides ChatML/Llama3/Mistral/
    Gemma/Phi/Qwen/Custom; system prompts; BOS/EOS; stop strings/
    tokens; add-generation-prompt; role labels
[ ] Advanced: JSON Mode, GBNF grammar, thinking-tag stripping,
    unload-after-idle, keep-loaded, trim-old-messages, auto-reduce-
    context, presets(save/load/reset)

PHASE 4: Security + Stability
[ ] No INTERNET (merged manifest, CI enforced post-build) [guards live]
[ ] Guard 2/3 live in CI already; re-check on release APK
[ ] Keystore AES-GCM encrypted DataStore
[ ] Tamper Detection (local signature self-check - deterrent only)
[ ] allowBackup=false + dataExtractionRules (shipped in Phase 1)
[ ] Zero telemetry / no Play Services (shipped)
[ ] :engine = crash containment + fd-only input + no DB
[ ] Memory Guard + Resource Estimator + heavy-model warning
[ ] Crash Handler (Java + native, signal-safe)
[ ] CPU fallback / auto-reduce context / Eco-Balanced-Performance
[ ] Unload model after app exit

PHASE 5: Final UI + Logging
[ ] Material You polish; Light/Dark/System (shipped basic)
[ ] Minimal custom Markdown (no heavy lib)
[ ] RTL + Persian + Vazirmatn (shipped); copy/edit/regenerate/search
[ ] Context Bar / Backend Diagnostics
[ ] Onboarding: unknown-sources guide (shipped basic)
[ ] Local logging (levels, rotation, export, in-app settings)

SUPPORTED MODELS
------------------------------------------------------------
Format: GGUF only. Architectures: LLaMA(1..3.2) / Mistral-Mixtral /
Gemma(1,2,3,3n) / Qwen(1..3) / Phi(2..4) / TinyLlama / StableLM / MPT /
Falcon / Baichuan / InternLM / Yi / DeepSeek / CodeLlama / StarCoder.
Quantizations: Q2_K..F16 (Q4_K_M recommended).
RAM guide: 2-3GB TinyLlama1.1B ~600MB · 3-4GB Gemma2B/Phi-2 ~1.5GB ·
4-6GB Qwen1.5-4B/Phi-3mini ~2.5GB · 6-8GB Mistral7B/Qwen2.5-7B ~4.5GB ·
8GB+ Llama3.1-8B/Gemma2-9B ~5-6GB. Mixtral excluded (mmap thrash).
RULE: weights + KV must fit available RAM (estimator warns).

LOGGING SYSTEM
------------------------------------------------------------
Levels: VERBOSE(dev)/DEBUG/INFO(default)/WARNING/ERROR/FATAL.
Logged YES: lifecycle, model load, setting changes, errors, backend,
memory, speed, crashes. Logged NO: message content, responses,
system prompts, personal info.
Files: filesDir/logs/{app,engine,crash,settings}.log, 5MB x 3 rotating.

SECURITY GUARANTEES
------------------------------------------------------------
- Zero INTERNET in MERGED manifest (CI post-build guard) [live]
- No network libs in release runtime classpath (CI guard) [live]
- No libcurl in ANY native binary (CI readelf/nm guard) [live]
- :engine gets ONLY an fd - no DB, no SAF, no UI; crash-contained
- Encrypted sensitive settings (Keystore AES-GCM, custom, Phase 4)
- Tamper Detection (deterrent, Phase 4)
- allowBackup=false + dataExtractionRules [live]
- Zero telemetry / no Play Services / local logs only

SPEED OPTIMIZATIONS
------------------------------------------------------------
Pure native; incremental prompt processing (KV reuse); 3-variant
runtime CPU dispatch via GGML_CPU_ALL_VARIANTS=ON; Vulkan + CPU
fallback; ggml threadpool (OPENMP OFF), LLAMA_CURL OFF; 16KB
alignment; mmap; quantized KV (auto FA); idle auto-unload; batched
token callbacks (R16).

DEVICE PROFILES
------------------------------------------------------------
ECO (2-4GB): threads 2-4, ctx 512-1024, GPU off, KV q4_0(FA), max256
BALANCED (4-6GB): threads 4-6, ctx 2048, GPU optional, KV q8_0(FA),
max 512
PERFORMANCE (8GB+): big cores, ctx 4096+, GPU on, KV f16, max 2048+

GITHUB ACTIONS + AUTOMATION (FIXED)
------------------------------------------------------------
PERMISSIONS: ci.yml {contents: read}; release.yml {contents: write};
submodule-bump.yml {contents: write}; keystore-bootstrap {contents:
read}. Workflow files are committed by the user (GitHub web UI),
never pushed by CI.

WORKFLOW 1 - ci.yml (push/PR) [LIVE]
  preflight: resolve every catalog pin (429-aware, fail on 404);
  verify Gradle dist URL; sdkmanager platform 36 + NDK r30; print
  declared R2/R4 pairing.
  build: checkout(submodules recursive) -> setup-java 21 ->
  sdkmanager platform 36 -> gradle assembleDebug test ->
  Guard 1 (manifest, fail-if-found) -> Guard 2 (releaseRuntimeClasspath)
  -> Guard 3 (every .so: readelf NEEDED + nm curl symbols,
  fail-if-found) -> upload debug APK artifact (14 days).
  concurrency: cancel-in-progress per ref.

WORKFLOW 2 - release.yml (tag vX.Y.Z) [DEFERRED TO FIRST TAG]
  preflight + assembleRelease (arm64-v8a) + zipalign -c -P 16 +
  apksigner (secrets KEYSTORE_BASE64/KEYSTORE_PASSWORD/KEY_ALIAS/
  KEY_PASSWORD) + all 3 guards on the RELEASE APK + gh release create.
  Pinned build-tools install step.

WORKFLOW 3 - submodule-bump.yml (dispatch + monthly) [LIVE]
  First run: git submodule add llama.cpp at
  engine/llama/third_party/llama.cpp; later runs move the gitlink.
  Commits to bot/llama-bump-<tag> branch ONLY + PR. Never main,
  never floating master.

WORKFLOW 4 - keystore-bootstrap.yml (one-time, manual) [LIVE]
  keytool generates PKCS12 keystore; values live ONLY inside a
  1-day artifact (never in logs); user stores secrets + deletes run.

RENOVATE (renovate.json) [LIVE]
  weekly, stable-only allowedVersions regex, git-submodules manager
  disabled, groups androidx/compose + build-toolchain + test-tooling;
  automerge androidx patch/minor only.

RELEASE QA CHECKLIST (MANUAL, ~30 MIN, BEFORE EVERY TAG)
------------------------------------------------------------
On one ECO, one BALANCED, one PERFORMANCE device (document results
in the release notes):
 1. Install signed APK via unknown-sources; onboarding completes
 2. Pick folder via SAF; non-local provider rejected with clear error
 3. Scan finds .gguf; registered WITHOUT copy (source folder unchanged)
 4. Load heaviest recommended model; estimator warns when RAM tight
 5. 10-min chat: streaming (R16 batches), stop, regenerate, rename/
    delete chat, tokens/sec visible
 6. Force-stop :engine mid-generation (adb) -> clean error, app
    recovers, no ANR
 7. Airplane mode ON the whole session - nothing breaks
 8. Zero INTERNET permission on installed build (dumpsys package)
 9. Idle 5 min -> model unloads; no wakelocks (dumpsys power)
10. Logs: level switch, rotation, export writes locally only

SUCCESS METRICS (REALISTIC)
------------------------------------------------------------
App RAM (excl. mmap weights) < 100 MB; cold start < 2 s; 2GB model
load (mmap) < 10 s; CPU <=3B Q4_K_M > 10 t/s; CPU 7B Q4_K_M > 3 t/s
(target 5); GPU high-end > 25 t/s (aspirational); first token warm
< 3 s; crashes during Release QA = 0 (documented manual checklist,
not a CI guarantee); no wakelocks, idle auto-unload.

DATA FLOW
------------------------------------------------------------
Model: SAF local folder -> persistable URI (re-checked at load) ->
scan .gguf -> Room register (NO copy) -> ModelsScreen ->
ResourceEstimator -> BackendSelector (Phase 2) ->
ParcelFileDescriptor -> Binder -> :engine -> /proc/self/fd/N (mmap
zero copy) -> GGUF chat template -> ready.
Chat: ChatViewModel -> SendPromptUseCase -> Room save -> AIDL oneway
(batched, R16) -> :engine -> incremental KV processing -> token
stream (batched flush) -> oneway callback -> callbackFlow +
conflate -> Compose streaming + t/s -> Room save. linkToDeath guards
the channel.

FINAL CHECKLIST
------------------------------------------------------------
[x] aura / com.aura.neon / minSdk 29 / targetSdk 34 FINAL
[x] compileSdk 36
[x] GitHub-only signed APK, arm64-v8a, 16 KB aligned (design)
[x] 12 modules + build-logic, acyclic graph, engine has no DB
[x] 3 CI guards live, fail-if-found, all .so scanned
[x] CI permissions fixed; preflight mandatory (429-aware)
[x] Renovate policy fixed; llama.cpp bump via dedicated workflow
[x] Phone-only: submodule + keystore bootstrap workflows
[x] Binder death handling + signal-safe crash handler (design)
[x] R16 token batching + R17 minify decision + R9 dispatch flags
[x] PLACEMENTS resolved; Release QA checklist defined
[x] Known-good fallback stack documented (Appendix A)
[x] All pins web-verified 2026-11-24 (24/24 probes green/OK-or-transient)
[x] Phase 1 features implemented AND CI-green (run 45328264207)
[ ] Phase 2-5 features per plan

APPENDIX A - KNOWN-GOOD FALLBACK STACK (VERIFIED v3.1)
------------------------------------------------------------
If preflight fails on any 2026 pin and a fix is not immediate, switch
the catalog to this verified-compatible toolchain (structure, modules,
guards and workflows remain unchanged):
  Gradle 8.10.2 / AGP 8.7.2 / JDK 17 / Kotlin 2.0.21 /
  KSP 2.0.21-1.0.28 (KSP1) / Compose BOM 2024.12.01 /
  core-ktx 1.13.1 / activity-compose 1.9.3 / lifecycle 2.8.7 /
  navigation-compose 2.8.5 / hilt-nav-compose 1.2.0 / Hilt 2.52 /
  coroutines 1.9.0 / datastore 1.1.1 / Room 2.6.1 / JUnit 4.13.2 /
  compileSdk 35 / targetSdk 34 / minSdk 29 /
  NDK: none in Phase 1; add in Phase 2.
  Turbine 1.2.1 and Robolectric 4.18.1 work with both stacks.
============================================================
END OF SPEC - Aura Neon v2.6 - 2026-11-24
============================================================
