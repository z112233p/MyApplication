package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.tools.IntentHelper
import kotlinx.android.synthetic.main.activity_chat_room_v3.*

class LaunchActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                IntentHelper.gotoEventActivity(this@LaunchActivity)
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {


            }
        }.start()
    }
}