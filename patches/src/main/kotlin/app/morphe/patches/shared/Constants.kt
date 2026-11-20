package app.morphe.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val BROBUDDY_PACKAGE_NAME = "com.brobuddy.game"
    const val BROBUDDY_TARGET_VERSION = "1.0.3"

    val COMPATIBILITY_BROBUDDY = Compatibility(
        name = "BroBuddy",
        packageName = BROBUDDY_PACKAGE_NAME,
        description = "BroBuddy game patch",
        apkFileType = ApkFileType.APK,
        appIconColor = 0xFF6B35,
        targets = listOf(
            AppTarget(
                version = BROBUDDY_TARGET_VERSION,
                isExperimental = false,
                description = "BroBuddy: Ragdoll Game v1.0.3",
            )
        )
    )

    const val YUKA_PACKAGE_NAME = "io.yuka.android"
    const val YUKA_TARGET_VERSION = "5.9.2"

    val COMPATIBILITY_YUKA = Compatibility(
        name = "Yuka",
        packageName = YUKA_PACKAGE_NAME,
        description = "Yuka - Food & Cosmetic Scanner patch",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x38D06C,
        targets = listOf(
            AppTarget(
                version = YUKA_TARGET_VERSION,
                isExperimental = false,
                description = "Yuka - Food & Cosmetic Scanner v5.9.2",
            )
        )
    )
}
