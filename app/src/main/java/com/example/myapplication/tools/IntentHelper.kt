package com.example.myapplication.tools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.myapplication.activity.ChatRoomActivity

object IntentHelper {


    fun gotoChatRoom(ctx: Context, rID: String) {
        val intent = Intent(ctx, ChatRoomActivity::class.java)
        val b = Bundle()
        b.putString("ChatRoomID", rID)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }
}