package com.illa.joliveapp.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.billingclient.api.*
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.AdapterPager
import com.illa.joliveapp.fragment.*
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.activity_myinfo.toolbar
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyInfoActivity : AppCompatActivity(), PurchasesUpdatedListener,CoroutineScope by MainScope() {
    val profileActivityVM: ProfileActivityVM by viewModel()

    private var f1 = FragmentMyinfo_info()
    private var f2 = FragmentMyinfo_event()
    private lateinit var actionItem: MenuItem
    private lateinit var optionItem: MenuItem
    private lateinit var reportItem: MenuItem

    private var currentPosition = 0
    var userLabel = ""

    private lateinit var purchasesUpdateListener: PurchasesUpdatedListener

    private lateinit var billingClient: BillingClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        getIntentData()
        init()
        connectPlayStore()
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)
    }

    private fun connectPlayStore(){
        purchasesUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            Log.e("Peter","onPurchasesUpdated2 IN   ${billingResult.debugMessage}")
            Log.e("Peter","onPurchasesUpdated2 IN   ${billingResult.responseCode}")

        }
        billingClient = BillingClient.newBuilder(this)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.e("Peter","billingClient.startConnection   OK")//product_black_chocolate_90
                    launch(Dispatchers.Main) {
                        querySkuDetails()
                    }
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    suspend fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("product_black_chocolate_90")
        skuList.add("test1")

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        withContext(Dispatchers.IO) {
            billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
                // Process the result.
                Log.e(
                    "Peter",
                    "billingClient.querySkuDetails   OK   billingResult   $billingResult."
                )
                Log.e(
                    "Peter",
                    "billingClient.querySkuDetails   OK   skuDetailsList   ${skuDetailsList?.get(0)?.price}"
                )
                Log.e(
                    "Peter",
                    "billingClient.querySkuDetails   OK   skuDetailsList   ${skuDetailsList?.get(0)?.description}"
                )
                Log.e(
                    "Peter",
                    "billingClient.querySkuDetails   OK   skuDetailsList   ${skuDetailsList?.get(0)?.title}"
                )
                Log.e(
                    "Peter",
                    "billingClient.querySkuDetails   OK   skuDetailsList   ${skuDetailsList?.get(1)}"
                )

                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsList?.get(0)!!)
                    .build()

//                val responseCode = billingClient.launchBillingFlow(this@MyInfoActivity, flowParams).responseCode
//                Log.e("Peter","billingClient.querySkuDetails   OK   launchBillingFlow   ${responseCode}")
                billingClient.queryPurchases("product_black_chocolate_90")
                queryPurchases()
            }
        }
    }

    fun queryPurchases() {
        if (!billingClient.isReady) {
            return
        }

        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

        Log.e("Peter","queryPurchases1   ${result.billingResult}")
        Log.e("Peter","queryPurchases2   ${result.purchasesList}")
        Log.e("Peter","queryPurchases3   ${result.responseCode}")


        if (result == null) {
            return
        }

        if (result.purchasesList == null) {
            return
        }

//        processPurchases(result.purchasesList!!)
    }

    private fun getIntentData(){
        val b = intent.extras
        userLabel = b?.getString("Label")!!
        currentPosition = b.getInt("position")

    }

    fun setCurrentPage(pagePosition: Int){
        vp_my_info.currentItem = pagePosition

    }

    private fun init(){
        Log.e("Peter","MyInfoActivity init")



        val tabTitle = mutableListOf<String>(*resources.getStringArray(R.array.my_info_pager))
        val myInfoFragments : ArrayList<Fragment> = arrayListOf()
        myInfoFragments.add(f1)
        myInfoFragments.add(f2)

        val pagerAdapter = AdapterPager(supportFragmentManager, tabTitle, myInfoFragments)
        vp_my_info.adapter = pagerAdapter
        vp_my_info.currentItem = currentPosition
        tab_my_info.setupWithViewPager(vp_my_info)


    }

    private fun initObserve(){

        profileActivityVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        actionItem = menu!!.findItem(R.id.action_edit)
        optionItem = menu.findItem(R.id.action_option)
        reportItem = menu.findItem(R.id.action_report)
        Log.e("Peter","MyInfoActivity onPrepareOptionsMenu")
        if(userLabel == PrefHelper.chatLable){
            actionItem.isVisible = true
            optionItem.isVisible = true
            reportItem.isVisible = false
        } else {
            actionItem.isVisible = false
            optionItem.isVisible = false
            reportItem.isVisible = true
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_info, menu)
        return true
    }

    @SuppressLint("Range")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        messagesList.scrollToPosition((messagesList.getChildAt (0).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition)
        return when (item.itemId) {
            R.id.action_option -> {
                Log.e("Peter","MyInfoActivity onOptionsItemSelected action_option")
                IntentHelper.gotoSettingActivity(this)

                false
            }
            R.id.action_edit -> {
                Log.e("Peter","MyInfoActivity onOptionsItemSelected action_edit")

                IntentHelper.gotoEditMyInfoActivity(this)
                false
            }
            R.id.action_report -> {

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("確定要檢舉？")
                builder.setPositiveButton("確定") {
                        p0, p1 -> Log.e("Peter","dialog ok")
                }
                builder.setNegativeButton("取消") {
                        p0, p1 -> Log.e("Peter","dialog cancel")
                }
                val dialog = builder.create()
                dialog.show()
                false
            }
            else -> false
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        Log.e("Peter","onPurchasesUpdated IN")

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
//                handlePurchase(purchase)
                Log.e("Peter","onPurchasesUpdated OK  ${purchases[0].purchaseToken}")
                Log.e("Peter","onPurchasesUpdated OK  ${purchases}")

            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.e("Peter","onPurchasesUpdated err  ${billingResult.responseCode}")
            Log.e("Peter","onPurchasesUpdated err  ${billingResult.debugMessage}")
            Log.e("Peter","onPurchasesUpdated err  ${purchases}")

        } else {
            // Handle any other error codes.
            Log.e("Peter","onPurchasesUpdated err  ${billingResult.responseCode}")
            Log.e("Peter","onPurchasesUpdated err  ${billingResult.debugMessage}")
            Log.e("Peter","onPurchasesUpdated err  ${purchases}")


        }
    }
}