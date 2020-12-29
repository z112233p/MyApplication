package com.illa.joliveapp.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.illa.joliveapp.R
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.activity_events.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class EventsActivity : AppCompatActivity(){
    private val eventsActivityVM :EventsActivityVM by viewModel()
    private lateinit var mContext: Context
    private var onBackID: Int = R.id.navigation_home
    private var hasAddDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)

        initObserve()
        val navController= findNavController(this,R.id.nav_host_fragment)

        // Hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)

        mContext = this
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        nav_view.itemIconTintList = null

        nav_view.setOnNavigationItemSelectedListener {
            when(it.itemId){
//                R.id.navigation_pair -> {
////                    IntentHelper.gotoProfileActivity(this)
//
//                    true
//                }
                R.id.navigation_chat -> {
                    if(navController.currentDestination?.id != R.id.FragmentChatRoom){
                        navController.popBackStack(navController.currentDestination!!.id,true)
                        navController.navigate(R.id.FragmentChatRoom)
                    }
                    onBackID = R.id.navigation_chat
                    true
                }
                R.id.navigation_home -> {
                    if(navController.currentDestination?.id != R.id.FragmentMain){
                        navController.popBackStack(navController.currentDestination!!.id,true)
                        navController.navigate(R.id.FragmentMain)
                    }
                    onBackID = R.id.navigation_home

                    true
                }
                R.id.navigation_notifications -> {
                    IntentHelper.gotoNoticeActivity(this)

                    true
                }
                R.id.navigation_personal -> {
                    IntentHelper.gotoMyInfoActivity(this, PrefHelper.chatLable)
                    true
                }
                else -> true
            }
        }

    }

    private fun addRedDot(){
        val botSize = nav_view.itemIconSize

        val menuItem: BottomNavigationMenuView = nav_view.getChildAt(0) as BottomNavigationMenuView
        val mTab = menuItem.getChildAt(2)
        val itemView = mTab as BottomNavigationItemView
        var mBadge = itemView.getChildAt(2)
        Log.e("Peter","removeRedDot  == null ADD   ${itemView.childCount}")

        if(mBadge == null){
            mBadge = LayoutInflater.from(nav_view.context).inflate(R.layout.item_badge, menuItem, false)
        }
        itemView.addView(mBadge)
        hasAddDot = true
    }

    private fun removeRedDot(){
        val botSize = nav_view.itemIconSize

        val menuItem: BottomNavigationMenuView = nav_view.getChildAt(0) as BottomNavigationMenuView
        val mTab = menuItem.getChildAt(2)
        val itemView = mTab as BottomNavigationItemView
        var mBadge = itemView.getChildAt(2)
        if(mBadge == null){
            Log.e("Peter","removeRedDot  == null    ${itemView.childCount}")
            mBadge = LayoutInflater.from(nav_view.context).inflate(R.layout.item_badge, menuItem, false)
        } else {
            Log.e("Peter","removeRedDot  else")

            itemView.removeView(mBadge)

        }
        hasAddDot = false
    }

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)

        Log.e("Peter","EventsActivity onResume")
        nav_view.selectedItemId = onBackID
        eventsActivityVM.getNotice()
    }



    private fun initObserve(){
        eventsActivityVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })

        eventsActivityVM.getNoticeData().observe(this, Observer {
            removeRedDot()
            it.data.forEach {
                if(!it.is_read && !hasAddDot){
                    Log.e("peter","it.data.forEach")
                    addRedDot()
                    return@forEach
                }
            }

        })

    }


}