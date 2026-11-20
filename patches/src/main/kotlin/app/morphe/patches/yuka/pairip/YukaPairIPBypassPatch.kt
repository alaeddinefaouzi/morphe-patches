package app.morphe.patches.yuka.pairip

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.Constants

/**
 * Bypasses PairIP (Google Play Integrity) anti-tamper and licensing protection.
 *
 * PairIP employs multiple layers of verification:
 * 1. SignatureCheck.verifySignatureMatches & verifyIntegrity: verifies APK signing certificate
 *    against hardcoded SHA-256 hashes.
 * 2. LicenseClient.checkLicense & initializeLicenseCheck: binds to Google Play Services to check
 *    installation source and licensing. If unauthorized, it launches LicenseActivity with a paywall intent.
 * 3. LicenseActivity.showPaywallAndCloseApp & onStart: invokes the Google Play PendingIntent which displays
 *    the "Get this app from Play : The app installed on your device is not recognised" prompt.
 *
 * This patch neutralizes all layers, allowing the modified APK to run without being blocked.
 */
private val verifySignatureMatchesFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/SignatureCheck;",
    name = "verifySignatureMatches",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
)

private val verifyIntegrityFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/SignatureCheck;",
    name = "verifyIntegrity",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

private val checkLicenseFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "checkLicense",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

private val initializeLicenseCheckFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "initializeLicenseCheck",
    returnType = "V",
    parameters = emptyList(),
)

private val licenseActivityOnStartFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "onStart",
    returnType = "V",
    parameters = emptyList(),
)

private val showPaywallAndCloseAppFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "showPaywallAndCloseApp",
    returnType = "V",
    parameters = emptyList(),
)

private val showErrorDialogFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "showErrorDialog",
    returnType = "V",
    parameters = emptyList(),
)

@Suppress("unused")
val yukaPairIPBypassPatch = bytecodePatch(
    name = "Yuka PairIP Anti-Tamper Bypass",
    description = "Disables PairIP (Google Play Integrity) signature and licensing verification that blocks modified APKs. Fixes the 'Get this app from Play' dialog on launch.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY_YUKA)

    execute {
        // 1. Force verifySignatureMatches to return true
        verifySignatureMatchesFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent()
        )

        // 2. Completely bypass SignatureCheck.verifyIntegrity
        verifyIntegrityFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 3. Neutralize LicenseClient.checkLicense so it never contacts Play Services or requests a paywall
        checkLicenseFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 4. Neutralize LicenseClient.initializeLicenseCheck
        initializeLicenseCheckFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 5. Neutralize LicenseActivity.onStart so if the activity ever launches, it immediately closes cleanly
        licenseActivityOnStartFingerprint.method.addInstructions(
            0,
            """
                invoke-virtual {p0}, Landroid/app/Activity;->finish()V
                return-void
            """.trimIndent()
        )

        // 6. Neutralize showPaywallAndCloseApp so the Google Play dialog is never presented
        showPaywallAndCloseAppFingerprint.method.addInstructions(
            0,
            """
                invoke-virtual {p0}, Landroid/app/Activity;->finish()V
                return-void
            """.trimIndent()
        )

        // 7. Neutralize showErrorDialog
        showErrorDialogFingerprint.method.addInstructions(
            0,
            """
                invoke-virtual {p0}, Landroid/app/Activity;->finish()V
                return-void
            """.trimIndent()
        )
    }
}
