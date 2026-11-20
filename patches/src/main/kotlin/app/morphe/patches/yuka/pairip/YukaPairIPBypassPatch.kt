package app.morphe.patches.yuka.pairip

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.Constants

/**
 * Bypasses PairIP (Google Play Integrity) anti-tamper and licensing protection.
 *
 * PairIP employs multiple layers of verification:
 * 1. StartupLauncher.launch & VMRunner.invoke: bootstraps the native libpairipcore.so VM engine.
 * 2. SignatureCheck.verifySignatureMatches & verifyIntegrity: verifies APK signing certificate
 *    against hardcoded SHA-256 hashes.
 * 3. LicenseClient.checkLicense, initializeLicenseCheck & performLocalInstallerCheck: binds to
 *    Google Play Services to check installation source (com.android.vending) and licensing.
 * 4. LicenseActivity (showPaywallAndCloseApp, showErrorDialog, closeApp, exitApp, onStart):
 *    handles and displays the "Get this app from Play" prompt.
 *
 * This patch neutralizes all layers, allowing the modified APK to run without being blocked.
 */
private val startupLauncherLaunchFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/StartupLauncher;",
    name = "launch",
    returnType = "V",
    parameters = emptyList(),
)

private val vmRunnerInvokeFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/VMRunner;",
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/String;", "[Ljava/lang/Object;"),
)

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

private val performLocalInstallerCheckFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "performLocalInstallerCheck",
    returnType = "Z",
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

private val closeAppFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "closeApp",
    returnType = "V",
    parameters = emptyList(),
)

private val exitAppFingerprint = Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "exitApp",
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
        // 1. Neutralize StartupLauncher.launch so VMRunner / libpairipcore.so is never started
        startupLauncherLaunchFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 2. Neutralize VMRunner.invoke to return null immediately
        vmRunnerInvokeFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return-object v0
            """.trimIndent()
        )

        // 3. Force verifySignatureMatches to return true
        verifySignatureMatchesFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent()
        )

        // 4. Completely bypass SignatureCheck.verifyIntegrity
        verifyIntegrityFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 5. Neutralize LicenseClient.checkLicense so it never contacts Play Services or requests a paywall
        checkLicenseFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 6. Neutralize LicenseClient.initializeLicenseCheck
        initializeLicenseCheckFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 7. Force performLocalInstallerCheck to always return true (pretend installed via com.android.vending)
        performLocalInstallerCheckFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent()
        )

        // 8. Neutralize LicenseActivity.onStart
        licenseActivityOnStartFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 9. Neutralize showPaywallAndCloseApp so the Google Play dialog is never presented
        showPaywallAndCloseAppFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 10. Neutralize showErrorDialog
        showErrorDialogFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        // 11. Neutralize closeApp & exitApp
        closeAppFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )

        exitAppFingerprint.method.addInstructions(
            0,
            """
                return-void
            """.trimIndent()
        )
    }
}
