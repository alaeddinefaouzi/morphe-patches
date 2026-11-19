package app.morphe.patches.brobuddy.ads

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.PatchException

object MaxInterstitialShowFingerprint : Fingerprint(
    definingClass = "Lcom/applovin/mediation/unity/MaxUnityAdManager;",
    name = "showInterstitial",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/String;", "Ljava/lang/String;")
)

object MaxRewardedAdReadyFingerprint : Fingerprint(
    definingClass = "Lcom/applovin/mediation/unity/MaxUnityAdManager;",
    name = "isRewardedAdReady",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;")
)

object MaxRewardedAdShowFingerprint : Fingerprint(
    definingClass = "Lcom/applovin/mediation/unity/MaxUnityAdManager;",
    name = "showRewardedAd",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/String;", "Ljava/lang/String;")
)

fun requireExactlyOne(matches: List<Any>?, target: String, patchName: String) {
    val count = matches?.size ?: 0
    if (count != 1) {
        throw PatchException("$patchName: $target matched $count time(s), expected exactly 1.")
    }
}
