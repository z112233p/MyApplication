package com.example.myapplication.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.tools.ProgressDialogController
import com.example.myapplication.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.activity_events.toolbar
import kotlinx.android.synthetic.main.fragment_choose_location.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class EventsActivity : AppCompatActivity(){
    private val eventsActivityVM :EventsActivityVM by viewModel()
    private lateinit var mContext: Context
    private var onBackID: Int = R.id.navigation_home
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setSupportActionBar(toolbar)
        initObserve()
        val navController= findNavController(this,R.id.nav_host_fragment)
        ProgressDialogController.setContext(this)

        // Hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)



        mContext = this
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        nav_view.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_pair -> {
//                    IntentHelper.gotoProfileActivity(this)

                    true
                }
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

    override fun onResume() {
        super.onResume()
        ProgressDialogController.setContext(this)
        Log.e("Peter","EventsActivity onResume")
        nav_view.selectedItemId = onBackID
    }



    private fun initObserve(){
        eventsActivityVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })
    }


}