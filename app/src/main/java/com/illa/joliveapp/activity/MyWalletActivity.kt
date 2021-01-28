package com.illa.joliveapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_myinfo.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyWalletActivity : AppCompatActivity(){
    val profileActivityVM: ProfileActivityVM by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        init()
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)
    }



    private fun init(){
        Log.e("Peter","MyInfoActivity init")


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

}