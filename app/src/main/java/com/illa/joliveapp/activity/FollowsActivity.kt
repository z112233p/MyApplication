package com.illa.joliveapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.AdapterPager
import com.illa.joliveapp.fragment.*
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.ProfileActivityVM
import kotlinx.android.synthetic.main.activity_follows.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowsActivity : AppCompatActivity() {
    val profileActivityVM: ProfileActivityVM by viewModel()

    private var f1 = FragmentFollowsFan("Follow")
    private var f2 = FragmentFollowsFan("Fan")
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follows)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        title = ""
        getIntentData()
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

    private fun getIntentData(){
        val b = intent.extras
        currentPosition = b!!.getInt("position")

    }

    private fun init(){
        val tabTitle = mutableListOf<String>(*resources.getStringArray(R.array.follow_tab))
        val myInfoFragments : ArrayList<Fragment> = arrayListOf()
        myInfoFragments.add(f1)
        myInfoFragments.add(f2)

        val pagerAdapter = AdapterPager(supportFragmentManager, tabTitle, myInfoFragments)
        vp_follows.adapter = pagerAdapter
        vp_follows.currentItem = currentPosition
        tab_follows.setupWithViewPager(vp_follows)

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