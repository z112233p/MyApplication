package com.illa.joliveapp.activity

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.create.CreateEvent
import com.illa.joliveapp.datamodle.event.detail.EventDetail
import com.illa.joliveapp.tools.ProgressDialogController
import com.illa.joliveapp.viewmodle.CreateEventsActivityVM
import kotlinx.android.synthetic.main.activity_creat_event.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CreateEventActivity  : AppCompatActivity() {

    val createEventsActVM: CreateEventsActivityVM by viewModel()
    var file : File = File("")
    val dataBody = CreateEvent()
    var eventLabel = ""
    var isEditMode = false
    lateinit var intentDataBody : EventDetail
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        setContentView(R.layout.activity_creat_event)
        title = ""
        navController= Navigation.findNavController(this, R.id.nav_host_fragment)

        getIntentData()
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getIntentData(){

        val b = intent.extras ?: return
        eventLabel = b.getString("Label")!!
        isEditMode = !TextUtils.isEmpty(eventLabel)
        createEventsActVM.getEventDetail(eventLabel)
        Log.e("Peter","CreateEventActivity getIntentData   $eventLabel")

    }

    override fun onResume() {
        super.onResume()
        Log.e("Peter","MainActivity onResume")
        ProgressDialogController.setContext(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
//        val fragmentManager = fragmentManager
//        fragmentManager.fragments.forEach {
//        }
        Log.e("Peter","CreateEventActivity onBackPressed  ")

        if(navController.currentDestination?.id == R.id.FragmentCreateEvent_v2){
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            if(isEditMode){
                builder.setMessage("取消更新活動？")

            } else {
                builder.setMessage("取消建立活動？")
            }
            builder.setPositiveButton("確定") {
                    p0, p1 -> super.onBackPressed()
            }
            builder.setNegativeButton("繼續編輯") {
                    p0, p1 -> Log.e("Peter","dialog cancel")
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            super.onBackPressed()
        }


    }

    private fun initObserve(){

        createEventsActVM.getEventDetail().observe(this, Observer {
            intentDataBody = it
        })

        createEventsActVM.getProgressStatus().observe(this, Observer {
            if(it){
                ProgressDialogController.dismissProgress()
            } else {
                ProgressDialogController.showProgress()
            }
        })
    }
    fun stepOne(){
        v_step_two.setBackgroundColor(this.resources.getColor(R.color.colorGray43))
        v_step_three.setBackgroundColor(this.resources.getColor(R.color.colorGray43))
    }

    fun stepTwo(){
        v_step_two.setBackgroundColor(this.resources.getColor(R.color.colorAccent))
        v_step_three.setBackgroundColor(this.resources.getColor(R.color.colorGray43))
    }

    fun stepThree(){
        v_step_two.setBackgroundColor(this.resources.getColor(R.color.colorAccent))
        v_step_three.setBackgroundColor(this.resources.getColor(R.color.colorAccent))
    }
}