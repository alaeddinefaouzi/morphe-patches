package app.morphe.patches.yuka.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.Constants

private val isLocalPremiumFingerprint = Fingerprint(
    definingClass = "Lio/yuka/android/service/user/NewUserService;",
    name = "isLocalPremium",
    returnType = "Z",
    parameters = emptyList(),
)

private val getLocalPremiumProviderFingerprint = Fingerprint(
    definingClass = "Lio/yuka/android/service/user/NewUserService;",
    name = "getLocalPremiumProvider",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
)

private val firestoreGetPremiumFingerprint = Fingerprint(
    definingClass = "Lio/yuka/android/service/user/model/FirestoreUser;",
    name = "getPremium",
    returnType = "Ljava/lang/Boolean;",
    parameters = emptyList(),
)

private val firestoreGetTopProductsFingerprint = Fingerprint(
    definingClass = "Lio/yuka/android/service/user/model/FirestoreUser;",
    name = "getTop_products",
    returnType = "Ljava/lang/Boolean;",
    parameters = emptyList(),
)

private val userRepositoryIsPremiumFingerprint = Fingerprint(
    definingClass = "Lkxg;",
    name = "h",
    returnType = "Z",
    parameters = emptyList(),
)

private val userRepositoryIsPremiumAsyncFingerprint = Fingerprint(
    definingClass = "Lkxg;",
    name = "g",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Lpf3;"),
)

@Suppress("unused")
val yukaPremiumPatch = bytecodePatch(
    name = "Yuka Plus Premium Unlock",
    description = "Unlocks all Yuka Member / Plus features including offline database scanning, unrestricted search, top product recommendations, and custom diet and allergen alerts without requiring a paid subscription.",
    default = true,
) {
    compatibleWith(Constants.COMPATIBILITY_YUKA)

    execute {
        // 1. Force local SharedPreferences premium state to true
        isLocalPremiumFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent()
        )

        // 2. Identify active subscription provider as Google Play
        getLocalPremiumProviderFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "google"
                return-object v0
            """.trimIndent()
        )

        // 3. Force Firestore user model to always return premium = true
        firestoreGetPremiumFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                return-object v0
            """.trimIndent()
        )

        // 4. Force top products / healthiest recommendations to true
        firestoreGetTopProductsFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                return-object v0
            """.trimIndent()
        )

        // 5. Central app-wide user repository check (queried by all activities)
        userRepositoryIsPremiumFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent()
        )

        // 6. Central asynchronous user repository check (coroutines)
        userRepositoryIsPremiumAsyncFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                return-object v0
            """.trimIndent()
        )
    }
}
