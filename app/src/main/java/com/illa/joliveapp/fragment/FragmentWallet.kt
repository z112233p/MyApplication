package com.illa.joliveapp.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.R
import com.illa.joliveapp.activity.NoticeActivity
import com.illa.joliveapp.adapter.Adapter_Follows
import com.illa.joliveapp.adapter.Adapter_Wallet
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.MainActivityVM
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.fragment_follows_fan.*
import kotlinx.android.synthetic.main.fragment_follows_fan.rv_follows_fan

import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import me.illa.jolive.adapter.Adapter_Notice
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class FragmentWallet : BaseFragment() {

    val profileActivityVM: ProfileActivityVM by activityViewModels()
    private lateinit var adapter: Adapter_Wallet



    override fun getLayoutId(): Int {
        return R.layout.fragment_wallet

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Peter","isNavigationViewInit ")

        if(!isNavigationViewInit){
            Log.e("Peter","isNavigationViewInit  ININ ")
            super.onViewCreated(view, savedInstanceState)
            init()
            setHasOptionsMenu(true)
            isNavigationViewInit = true
        }
    }

    override fun onResume() {
        super.onResume()
        getMContext().get()?.let { ProgressDialogController.setContext(it) }
        initObserve()
        callApis()
        setTitle("錢包")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun init(){
        adapter = Adapter_Wallet(getMContext().get())

        rv_wallet.layoutManager = LinearLayoutManager(getMContext().get(), RecyclerView.VERTICAL, false)
        rv_wallet.adapter = adapter

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ShowToast")
    private val onClick = View.OnClickListener {
        when (it.id) {

        }
    }

    private fun callApis(){
        profileActivityVM.getWallet()
    }

    private fun initObserve(){
        profileActivityVM.getWalletResponse().observe(viewLifecycleOwner, Observer {
            adapter.setData(it.data.COIN_1.history)
            tv_amount.text = it.data.COIN_1.total.toString()
        })
    }

}
