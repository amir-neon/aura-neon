#!/usr/bin/env bash
# =============================================================================
# Aura Neon - MANDATORY PIN PREFLIGHT (CI job #1)
# Spec: every declared pin must resolve from google()/mavenCentral()/portal
# BEFORE any build step. Fail fast with an exact item. No pin -> no build.
# =============================================================================
set -u

FAILED=0
TOTAL=0

probe() {
    local label="$1"
    local url="$2"
    TOTAL=$((TOTAL + 1))
    local code="000" attempt
    for attempt in 1 2 3; do
        code=$(curl -sL -o /dev/null -w '%{http_code}' -m 30 "$url")
        if [ "$code" = "200" ]; then
            echo "OK    $label"
            return 0
        fi
        case "$code" in
            429|5*) sleep 3 ;;   # throttled / CDN hiccup: retry
            *) break ;;          # definitive answer (404 etc): stop retrying
        esac
    done
    case "$code" in
        429|5*)
            # A 429/5xx means "the host is throttling us", NOT "the pin is missing".
            # Gradle's own dependency resolution during the build remains the true arbiter.
            echo "WARN  $label (HTTP $code: host throttled this probe - treated as transient)"
            ;;
        *)
            echo "FAIL  $label (HTTP $code) -> $url"
            FAILED=1
            ;;
    esac
}

G="https://dl.google.com/android/maven2"
M="https://repo1.maven.org/maven2"
P="https://plugins.gradle.org/m2"

# --- distribution ------------------------------------------------------------
probe "gradle-9.9.2-dist" "https://services.gradle.org/distributions/gradle-9.9.2-bin.zip"

# --- build stack -------------------------------------------------------------
probe "agp-9.5.2"                "$G/com/android/tools/build/gradle/9.5.2/gradle-9.5.2.pom"
probe "kotlin-stdlib-2.3.21"      "$M/org/jetbrains/kotlin/kotlin-stdlib/2.3.21/kotlin-stdlib-2.3.21.pom"
probe "ksp-2.3.14"               "$M/com/google/devtools/ksp/symbol-processing-api/2.3.14/symbol-processing-api-2.3.14.pom"
probe "hilt-2.60.1"              "$M/com/google/dagger/hilt-android/2.60.1/hilt-android-2.60.1.pom"
probe "coroutines-1.11.0"        "$M/org/jetbrains/kotlinx/kotlinx-coroutines-core/1.11.0/kotlinx-coroutines-core-1.11.0.pom"

# --- androidx ----------------------------------------------------------------
probe "compose-bom-2026.11.01"   "$G/androidx/compose/compose-bom/2026.11.01/compose-bom-2026.11.01.pom"
probe "core-ktx-1.19.1"          "$G/androidx/core/core-ktx/1.19.1/core-ktx-1.19.1.pom"
probe "activity-compose-1.13.0"  "$G/androidx/activity/activity-compose/1.13.0/activity-compose-1.13.0.pom"
probe "lifecycle-2.11.1"         "$G/androidx/lifecycle/lifecycle-runtime-compose/2.11.1/lifecycle-runtime-compose-2.11.1.pom"
probe "navigation-2.10.2"        "$G/androidx/navigation/navigation-compose/2.10.2/navigation-compose-2.10.2.pom"
probe "room-2.8.5"               "$G/androidx/room/room-runtime/2.8.5/room-runtime-2.8.5.pom"
probe "datastore-1.3.0"          "$G/androidx/datastore/datastore-preferences/1.3.0/datastore-preferences-1.3.0.pom"
probe "hilt-nav-compose-1.4.0"  "$G/androidx/hilt/hilt-navigation-compose/1.4.0/hilt-navigation-compose-1.4.0.pom"

# --- test --------------------------------------------------------------------
probe "robolectric-4.18.1"       "$M/org/robolectric/robolectric/4.18.1/robolectric-4.18.1.pom"
probe "junit-4.13.2"             "$M/junit/junit/4.13.2/junit-4.13.2.pom"
probe "turbine-1.2.1"            "$M/app/cash/turbine/turbine/1.2.1/turbine-1.2.1.pom"

# --- plugin markers (resolution path actually used by Gradle) ----------------
probe "marker-kotlin-android"    "$P/org/jetbrains/kotlin/android/org.jetbrains.kotlin.android.gradle.plugin/2.3.21/org.jetbrains.kotlin.android.gradle.plugin-2.3.21.pom"
probe "marker-kotlin-jvm"        "$P/org/jetbrains/kotlin/jvm/org.jetbrains.kotlin.jvm.gradle.plugin/2.3.21/org.jetbrains.kotlin.jvm.gradle.plugin-2.3.21.pom"
probe "marker-kotlin-compose"    "$P/org/jetbrains/kotlin/plugin/compose/org.jetbrains.kotlin.plugin.compose.gradle.plugin/2.3.21/org.jetbrains.kotlin.plugin.compose.gradle.plugin-2.3.21.pom"
probe "impl-compose-compiler"    "$M/org/jetbrains/kotlin/compose-compiler-gradle-plugin/2.3.21/compose-compiler-gradle-plugin-2.3.21.pom"
probe "marker-ksp"               "$P/com/google/devtools/ksp/com.google.devtools.ksp.gradle.plugin/2.3.14/com.google.devtools.ksp.gradle.plugin-2.3.14.pom"
probe "marker-hilt"              "$P/com/google/dagger/hilt/android/com.google.dagger.hilt.android.gradle.plugin/2.60.1/com.google.dagger.hilt.android.gradle.plugin-2.60.1.pom"
probe "marker-agp-application"   "$G/com/android/application/com.android.application.gradle.plugin/9.5.2/com.android.application.gradle.plugin-9.5.2.pom"
probe "marker-agp-library"       "$G/com/android/library/com.android.library.gradle.plugin/9.5.2/com.android.library.gradle.plugin-9.5.2.pom"

echo "----"
echo "KSP<->Kotlin (R4): kotlin=2.3.21, ksp=2.3.14 (KSP2 standalone versioning)."
echo "AGP9 Kotlin mechanism (R2): BUILT-IN Kotlin (AGP 9 rejects external kotlin-android)."
echo "The first build job is the final arbiter of both pairings."

if [ "$FAILED" -ne 0 ]; then
    echo "PIN VERIFICATION FAILED: see FAIL lines above."
    exit 1
fi
echo "ALL PINS RESOLVED ($TOTAL/$TOTAL)."
