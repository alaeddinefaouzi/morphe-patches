package app.morphe.patches.brobuddy.billing

import app.morphe.patcher.patch.RawResourcePatch
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.all.misc.hex.hexPatch
import app.morphe.patches.shared.Constants

private const val LIB = "lib/arm64-v8a/libil2cpp.so"

@Suppress("unused")
val broBuddyNativeIapPatch: RawResourcePatch = rawResourcePatch(
    name = "BroBuddy Native IAP Verification Bypass",
    description = "Bypasses client-side receipt and signature verification in the game's native engine, allowing free store purchases to be validated and credited instantly.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY_BROBUDDY)

    dependsOn(
        hexPatch {
            // 1. ValidatePurchaseIfPossible (0x4483994)
            "FE 0F 1E F8 F4 4F 01 A9 F3 03 01 AA F4 03 00 AA C8 1D 00 94 C0 00 00 36 E0 03 14 AA E1 03 13 AA".asPatternTo(
                "20 00 80 52 C0 03 5F D6 F3 03 01 AA F4 03 00 AA C8 1D 00 94 C0 00 00 36 E0 03 14 AA E1 03 13 AA"
            ).inFile(LIB)

            // 2. CanCrossPlatformValidate (0x448b0c4)
            "FE 0F 1E F8 F4 4F 01 A9 74 5F 02 90 F3 03 00 AA 88 82 40 39 C8 00 00 37 C0 31 02 F0 00 30 46 F9".asPatternTo(
                "00 00 80 52 C0 03 5F D6 74 5F 02 90 F3 03 00 AA 88 82 40 39 C8 00 00 37 C0 31 02 F0 00 30 46 F9"
            ).inFile(LIB)

            // 3. OnConfirmationFailed (0x4483bdc)
            "FF 83 01 D1 FE 67 02 A9 F8 5F 03 A9 F6 57 04 A9 F4 4F 05 A9 95 5F 02 F0 F3 03 01 AA F4 03 00 AA A8 92 7F 39 A8 02 00 37".asPatternTo(
                "E2 00 00 14 FE 67 02 A9 F8 5F 03 A9 F6 57 04 A9 F4 4F 05 A9 95 5F 02 F0 F3 03 01 AA F4 03 00 AA A8 92 7F 39 A8 02 00 37"
            ).inFile(LIB)

            // 4. IsHaveNoAds (0x3f20210)
            "FE 0F 1D F8 F6 57 01 A9 F4 4F 02 A9 94 8A 02 F0 75 5D 02 B0 F3 03 00 AA 88 66 4F 39 B5 6E 40 F9".asPatternTo(
                "20 00 80 52 C0 03 5F D6 F4 4F 02 A9 94 8A 02 F0 75 5D 02 B0 F3 03 00 AA 88 66 4F 39 B5 6E 40 F9"
            ).inFile(LIB)

            // 5. HaveVipPack (0x3f20a08)
            "FE 5F BD A9 F6 57 01 A9 F4 4F 02 A9 14 34 40 F9 74 03 00 B4 88 0E 40 F9 1F 01 00 71 F5 D7 9F 1A".asPatternTo(
                "20 00 80 52 C0 03 5F D6 F4 4F 02 A9 14 34 40 F9 74 03 00 B4 88 0E 40 F9 1F 01 00 71 F5 D7 9F 1A"
            ).inFile(LIB)

            // 6. IsHaveSkipAds (0x3f2031c)
            "E0 03 1F AA 2C 34 03 14 00 E0 41 39 C0 03 5F D6".asPatternTo(
                "20 00 80 52 C0 03 5F D6 00 E0 41 39 C0 03 5F D6"
            ).inFile(LIB)

            // 7. IsBannerEnabled (0x458f824)
            "FE 0F 1E F8 F4 4F 01 A9 54 57 02 90 F3 29 02 D0 88 8E 66 39 73 BE 41 F9 C8 00 00 37 E0 29 02 D0".asPatternTo(
                "00 00 80 52 C0 03 5F D6 54 57 02 90 F3 29 02 D0 88 8E 66 39 73 BE 41 F9 C8 00 00 37 E0 29 02 D0"
            ).inFile(LIB)

            // 8. CreateBannerAd (0x458f460)
            "FE 0F 1E F8 F4 4F 01 A9 53 57 02 90 F4 29 02 D0 68 7A 66 39 94 C2 41 F9 28 01 00 37 E0 29 02 D0".asPatternTo(
                "C0 03 5F D6 F4 4F 01 A9 53 57 02 90 F4 29 02 D0 68 7A 66 39 94 C2 41 F9 28 01 00 37 E0 29 02 D0"
            ).inFile(LIB)

            // 9. ShowBanner_4904 (0x458f580)
            "FE 57 BE A9 F4 4F 01 A9 54 57 02 90 F3 29 02 B0 88 7E 66 39 73 02 47 F9 08 03 00 37 E0 29 02 D0".asPatternTo(
                "C0 03 5F D6 F4 4F 01 A9 54 57 02 90 F3 29 02 B0 88 7E 66 39 73 02 47 F9 08 03 00 37 E0 29 02 D0"
            ).inFile(LIB)

            // 10. ShowBanner_878 (0x3e46de8)
            "FE 0F 1E F8 F4 4F 01 A9 73 91 02 90 34 64 02 D0 68 C6 6C 39 94 02 47 F9 C8 00 00 37 20 64 02 D0".asPatternTo(
                "C0 03 5F D6 F4 4F 01 A9 73 91 02 90 34 64 02 D0 68 C6 6C 39 94 02 47 F9 C8 00 00 37 20 64 02 D0"
            ).inFile(LIB)
        }
    )
}
