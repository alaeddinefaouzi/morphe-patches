package app.morphe.patches.yuka.pairip

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.Constants

/**
 * Bypasses PairIP (Google Play Integrity) anti-tamper protection by patching
 * SignatureCheck.verifySignatureMatches to always return true.
 *
 * PairIP verifies the APK's signing certificate against hardcoded Base64-encoded SHA-256 hashes.
 * When the APK is modified (patched), the signature changes and verification fails,
 * triggering the "Get this app from Play" error dialog.
 *
 * This patch makes verifySignatureMatches always return true, effectively disabling
 * the signature verification check.
 */
private val verifySignatureMatchesFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/SignatureCheck;",
    name = "verifySignatureMatches",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
)

@Suppress("unused")
val yukaPairIPBypassPatch = bytecodePatch(
    name = "Yuka PairIP Anti-Tamper Bypass",
    description = "Disables PairIP (Google Play Integrity) signature verification that prevents modified APKs from running. This patch is REQUIRED for the patched Yuka APK to work - without it, the app will show 'Get this app from Play' error on launch.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY_YUKA)

    execute {
        // Patch verifySignatureMatches to always return true (0x1)
        // This bypasses the APK signature check against hardcoded expected signatures
        verifySignatureMatchesFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent()
        )
    }
}