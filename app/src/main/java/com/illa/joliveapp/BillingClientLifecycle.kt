package com.illa.joliveapp

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.android.billingclient.api.*
import kotlinx.coroutines.*

class BillingClientLifecycle private constructor(
    private val app: Application
) : LifecycleObserver,
    PurchasesUpdatedListener,
    BillingClientStateListener {

    private lateinit var billingClient: BillingClient

    companion object {
        @Volatile
        private var INSTANCE: BillingClientLifecycle? = null

        fun getInstance(app: Application): BillingClientLifecycle =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BillingClientLifecycle(app)
                    .also { INSTANCE = it }
            }
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        TODO("Not yet implemented")
    }

    override fun onBillingServiceDisconnected() {
        TODO("Not yet implemented")
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage

//        PLog.d("$TAG:
//                onBillingSetupFinished: $responseCode $debugMessage")

        if (responseCode == BillingClient.BillingResponseCode.OK) {
            // The billing client is ready.
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    querySkuDetails()
//                    queryPurchases()
                } catch (e: Throwable) {
//                    PLog.e(e, "$TAG query:")
                } finally {
                    this.cancel()
                }
            }
        }
    }

    suspend fun querySkuDetails() {
//        PLog.d("$TAG: querySkuDetails")

        val params = SkuDetailsParams.newBuilder()
            .setType(BillingClient.SkuType.SUBS)
            .setSkusList(
//                listOf(SKU.Test001.id, SKU.Test002.id)

                listOf("SKU.Test001.id", "SKU.Test002.id")
            ).build()

        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params)
        }
        val billingResult = skuDetailsResult.billingResult
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
//        PLog.d("$TAG:
//                onSkuDetailsResponse: $responseCode $debugMessage")

//        PLog.obj(skuDetailsResult)

        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                skuDetailsResult.skuDetailsList?.also {
//                    skusWithSkuDetails.postValue(
//                        HashMap<String, SkuDetails>().apply {
//                            it.forEach {
//                                put(it.sku, it)
//                            }
//                        }.also {
////                            PLog.d("$TAG: onSkuDetailsResponse:
////                                    count ${it.size}")
//                        }
//                    )
                } ?: also {
//                    PLog.d("$TAG: onSkuDetailsResponse:
//                        null SkuDetails list")
//                                skusWithSkuDetails.postValue(emptyMap())
                }
            }
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED,
            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE,
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE,
            BillingClient.BillingResponseCode.DEVELOPER_ERROR,
            BillingClient.BillingResponseCode.ERROR -> {
//                PLog.e("$TAG: onSkuDetailsResponse:
//                    $responseCode $debugMessage")
            }
            BillingClient.BillingResponseCode.USER_CANCELED,
            BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED,
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED,
            BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                // These response codes are not expected.
//                PLog.d("$TAG: onSkuDetailsResponse:
//                    $responseCode $debugMessage")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
//        PLog.d("$TAG: ON_CREATE")

        billingClient = BillingClient
            .newBuilder(app.applicationContext)
            .setListener(this)
            .enablePendingPurchases() // Not used for subscriptions.
            .build()

        if (!billingClient.isReady) {
//            PLog.d("$TAG: BillingClient: Start connection...")
            billingClient.startConnection(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
//        PLog.d("$TAG: ON_DESTROYOY")

        if (billingClient.isReady) {
//            PLog.d("$TAG: closing connection")
            // After calling endConnection()
            // we must create a new BillingClient.
            billingClient.endConnection()
        }
    }
}