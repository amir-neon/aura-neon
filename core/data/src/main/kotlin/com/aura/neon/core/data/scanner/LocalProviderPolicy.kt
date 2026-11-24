package com.aura.neon.core.data.scanner

import android.net.Uri

/**
 * Non-local (cloud) document-provider rejection (spec R13).
 *
 * HONEST SCOPE: Android has no API that says "this provider is cloud-backed",
 * so this is a curated deny-list of well-known cloud authorities. Selecting a
 * tree backed by one of them is rejected with a clear error. The list grows
 * as users report providers - it is a policy, not a guarantee.
 */
object LocalProviderPolicy {

    private val NON_LOCAL_AUTHORITIES = setOf(
        "com.google.android.apps.docs", // Google Drive
        "com.dropbox.android",          // Dropbox
        "com.microsoft.skydrive",       // OneDrive
        "com.box.android",              // Box
        "com.pcloud.pcloud",            // pCloud
    )

    fun isNonLocalProvider(treeUri: Uri): Boolean {
        val authority = treeUri.authority ?: return false
        return NON_LOCAL_AUTHORITIES.any { authority.startsWith(it) }
    }
}
