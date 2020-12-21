package com.illa.joliveapp.adapter.holder

import android.view.View
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.chat_room.Message

class CustomInComingTextMessageViewHolder(itemView: View) : BaseMessageViewHolder(itemView) {
    override fun onBind(message: Message) {
        llMessageMain.background = MyApp.get()?.resources?.getDrawable(R.drawable.bg_incoming_msg_bubble)
        super.onBind(message)

    }
}