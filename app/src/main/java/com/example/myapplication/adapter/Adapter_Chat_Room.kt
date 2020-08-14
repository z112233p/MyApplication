package com.example.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.datamodle.chat.ChatRoomListUpdate
import com.example.myapplication.tools.IntentHelper
import com.example.myapplication.tools.PrefHelper
import org.json.JSONObject

class Adapter_Chat_Room() :
    RecyclerView.Adapter<Adapter_Chat_Room.ViewHolder>() {

    private lateinit var dataList: MutableList<ChatRoomListUpdate>
    private lateinit var mContext: Context


    constructor(context: Context) : this(){
        dataList = ArrayList<ChatRoomListUpdate>()
        mContext = context

    }

    fun setData(dealData: List<ChatRoomListUpdate>?) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    private fun dealChatRoomName(data: ChatRoomListUpdate): String{
        val jsonString = data.topic ?: "null"

        if ("null" == jsonString){
            return ""
        }
        Log.e("Peter", "jsonObject   $jsonString")
        Log.e("Peter", "jsonObject data._id  ${data._id}")

        val jsonObject = JSONObject(jsonString)

        return if (jsonObject.has(PrefHelper.getChatLable())){

            jsonObject.getString(PrefHelper.getChatLable())
        } else {
            if (jsonObject.has("name")){
                jsonObject.getString("name")
            } else {
                ""
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvChatRoomName: TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_Chat_Room.ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_chat_room, parent, false)
        val viewHolder = ViewHolder(cell)
        cell.layoutParams.height = 300
        viewHolder.tvChatRoomName = cell.findViewById(R.id.tv_chat_room_name)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Adapter_Chat_Room.ViewHolder, position: Int) {
        if(dataList.size == 0){return}
        Log.e("Peter","CHATADAPTER:   "+ dataList[position].topic)
        holder.tvChatRoomName.text = ""
        holder.tvChatRoomName.text = dealChatRoomName(dataList[position])
        holder.itemView.setOnClickListener {
            IntentHelper.gotoChatRoom(mContext, dataList[position]._id)
        }
    }

}