package com.example.myapplication.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.viewmodle.EventsActivityVM
import kotlinx.android.synthetic.main.activity_events.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class EventsActivity : AppCompatActivity(){
    private val eventsActivityVM :EventsActivityVM by viewModel()
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        setSupportActionBar(toolbar)

        val navController= findNavController(this,R.id.nav_host_fragment)

        mContext = this
        nav_view.selectedItemId = R.id.navigation_home

        nav_view.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_pair -> {
                    true
                }
                R.id.navigation_chat -> {
                    if(navController.currentDestination?.id != R.id.FragmentChatRoom){
                        navController.popBackStack(navController.currentDestination!!.id,true)
                        navController.navigate(R.id.FragmentChatRoom)
                    }
                    true
                }
                R.id.navigation_home -> {
                    if(navController.currentDestination?.id != R.id.FragmentMain){
                        navController.popBackStack(navController.currentDestination!!.id,true)
                        navController.navigate(R.id.FragmentMain)
                    }

                    true
                }
                R.id.navigation_notifications -> {
                    true
                }
                R.id.navigation_personal -> {
                    IntentHelper.gotoPersonalActivity(this)
                    true
                }
                else -> true
            }
        }

    }



}