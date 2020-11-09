package com.example.myapplication.adapter.holder

import android.util.Log
import android.view.View
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.tools.ImgHelper

class CustomOutComingTextMessageViewHolder(itemView: View) : BaseMessageViewHolder(itemView) {
    override fun onBind(message: Message) {
        llMessageMain.background = MyApp.get()?.resources?.getDrawable(R.drawable.bg_outcoming_msg_bubble)
        super.onBind(message)

    }
}