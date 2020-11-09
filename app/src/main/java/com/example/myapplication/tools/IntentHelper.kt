package com.example.myapplication.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.myapplication.activity.ChatRoomActivity
import com.example.myapplication.activity.EventsActivity
import com.example.myapplication.activity.MainActivity
import com.example.myapplication.activity.MapsActivity

object IntentHelper {


    fun gotoChatRoom(ctx: Context, rID: String) {
        val intent = Intent(ctx, ChatRoomActivity::class.java)
        val b = Bundle()
        b.putString("ChatRoomID", rID)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoEventActivity(ctx: Context){
        val intent = Intent(ctx, EventsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION;

        ctx.startActivity(intent)
//        (ctx as Activity).overridePendingTransition(0, 0)

    }

    fun gotoMapsActivity(ctx: Context){
        val intent = Intent(ctx, MapsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoPersonalActivity(ctx: Context){
        val intent = Intent(ctx, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }
}