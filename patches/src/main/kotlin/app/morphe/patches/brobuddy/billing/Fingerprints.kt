package app.morphe.patches.brobuddy.billing

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.PatchException

object BillingClientImplLaunchBillingFlowFingerprint : Fingerprint(
    definingClass = "Lcom/android/billingclient/api/BillingClientImpl;",
    name = "launchBillingFlow",
    returnType = "Lcom/android/billingclient/api/BillingResult;",
    parameters = listOf("Landroid/app/Activity;", "Lcom/android/billingclient/api/BillingFlowParams;")
)

object BillingClientImplAcknowledgePurchaseFingerprint : Fingerprint(
    definingClass = "Lcom/android/billingclient/api/BillingClientImpl;",
    name = "acknowledgePurchase",
    returnType = "V",
    parameters = listOf("Lcom/android/billingclient/api/AcknowledgePurchaseParams;", "Lcom/android/billingclient/api/AcknowledgePurchaseResponseListener;")
)

object BillingClientImplConsumeAsyncFingerprint : Fingerprint(
    definingClass = "Lcom/android/billingclient/api/BillingClientImpl;",
    name = "consumeAsync",
    returnType = "V",
    parameters = listOf("Lcom/android/billingclient/api/ConsumeParams;", "Lcom/android/billingclient/api/ConsumeResponseListener;")
)

object BillingClientImplQueryProductDetailsAsyncFingerprint : Fingerprint(
    definingClass = "Lcom/android/billingclient/api/BillingClientImpl;",
    name = "queryProductDetailsAsync",
    returnType = "V",
    parameters = listOf("Lcom/android/billingclient/api/QueryProductDetailsParams;", "Lcom/android/billingclient/api/ProductDetailsResponseListener;")
)

object BillingClientImplQueryPurchasesAsyncFingerprint : Fingerprint(
    definingClass = "Lcom/android/billingclient/api/BillingClientImpl;",
    name = "queryPurchasesAsync",
    returnType = "V",
    parameters = listOf("Lcom/android/billingclient/api/QueryPurchasesParams;", "Lcom/android/billingclient/api/PurchasesResponseListener;")
)

fun requireExactlyOne(matches: List<Any>?, target: String) {
    val count = matches?.size ?: 0
    if (count != 1) {
        throw PatchException("BroBuddy Free Store: $target matched $count time(s), expected exactly 1.")
    }
}
