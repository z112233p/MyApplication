package com.example.myapplication.adapter;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.datamodle.chat_room.Message;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.ViewHolder;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Chat_Room_Message<MESSAGES extends IMessage > extends MessagesListAdapter<MESSAGES> {

    private Message Message;
    protected List<Wrapper2> items2;
    private PeterHolder holders;
    private ImageLoader imageLoader;
    private String responseString = "false";



    public Adapter_Chat_Room_Message(String senderId, ImageLoader imageLoader) {
        super(senderId, imageLoader);
    }

    public Adapter_Chat_Room_Message(String senderId, MessageHolders holders, ImageLoader imageLoader) {

        super(senderId, holders, imageLoader);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List< Object > payloads) {
//        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()){
            onBindViewHolder((ViewHolder) holder, position);
            Log.e("Peter","Adapter_Chat_Room_Message onBindViewHolder  1");

        } else {
            ((TextView)holder.itemView.findViewById(R.id.tv_status)).setText(responseString);
            Log.e("Peter","Adapter_Chat_Room_Message onBindViewHolder  2");
        }
    }


    public class Wrapper2<DATA extends Wrapper>{
        public DATA item;
        public boolean isSelected;

        Wrapper2(DATA item) {
            this.item = item;
        }
    }

    public boolean update(MESSAGES message, boolean updateData) {
        if(updateData){
            return update(message.getId(), message);

        } else {
            responseString = ((Message)message).getSuccess();
            return update(message.getId(), message, responseString);

        }
    }

    public boolean update(String oldId, MESSAGES newMessage, String updateData) {
        this.items2 = new ArrayList<>();
        for (int i =0; i < items.size(); i++){
            items2.add(new Wrapper2(items.get(i)));
        }
        int position = getMessagePositionById(oldId);
        if (position >= 0) {

            notifyItemChanged(position,"qqq886");
            return true;
        } else {
            return false;
        }
    }

    private int getMessagePositionById(String id) {
        for (int i = 0; i < items.size(); i++) {
            MessagesListAdapter.Wrapper wrapperPeter = (MessagesListAdapter.Wrapper) items.get(i);
//            Wrapper2<Wrapper> element = new Wrapper2<>((Wrapper) wrapperPeter.item);
//            Log.e("Peter","Adapter_Chat_Room_Message getMessagePositionById  "+((MESSAGE)wrapperPeter.item).getId());

            if (((MESSAGES)wrapperPeter.item).getId().contentEquals(id)) {
                return i;
            }
        }
        return -1;
    }

}
