package com.example.myapplication.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.datamodle.event.create.CreateEvent
import com.example.myapplication.datamodle.event.detail.EventDetail
import com.example.myapplication.tools.ProgressDialogController
import com.example.myapplication.viewmodle.CreateEventsActivityVM
import com.example.myapplication.viewmodle.MainActivityVM
import kotlinx.android.synthetic.main.activity_creat_event.*
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CreateEventActivity  : AppCompatActivity() {

    val createEventsActVM: CreateEventsActivityVM by viewModel()
    var file : File = File("")
    val dataBody = CreateEvent()
    var eventLabel = ""
    lateinit var intentDataBody : EventDetail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        ProgressDialogController.setContext(this)
        setContentView(R.layout.activity_creat_event)
        title = ""
        getIntentData()
        initObserve()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getIntentData(){

        val b = intent.extras ?: return
        eventLabel = b?.getString("Label")!!
        createEventsActVM.getEventDetail(eventLabel)
        Log.e("Peter","CreateEventActivity getIntentData   $eventLabel")

    }

    override fun onResume() {
        super.onResume()
        Log.e("Peter","MainActivity onResume")
        ProgressDialogController.setContext(this)
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