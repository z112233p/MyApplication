package com.example.myapplication.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.datamodle.chat_room.Message;
import com.stfalcon.chatkit.messages.MessageHolders;

public class IncomingVoiceMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private TextView tvDuration;
    private TextView tvTime;

    public IncomingVoiceMessageViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

    }
}
