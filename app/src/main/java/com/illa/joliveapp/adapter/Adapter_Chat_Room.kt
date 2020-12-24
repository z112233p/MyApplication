package com.illa.joliveapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.chat.chatroom_list.Update
import com.illa.joliveapp.datamodle.chat.text_message.message_entry.MessageEntry
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.IntentHelper
import com.illa.joliveapp.tools.PrefHelper
import com.google.gson.Gson
import com.stfalcon.chatkit.utils.DateFormatter
import org.json.JSONObject
import java.text.SimpleDateFormat

@Suppress("SENSELESS_COMPARISON")
class Adapter_Chat_Room() : RecyclerView.Adapter<Adapter_Chat_Room.ViewHolder>() {

    @SuppressLint("SimpleDateFormat")
    private var customDateFormat: SimpleDateFormat = SimpleDateFormat("MM月dd日")
    private var customTodayFormat: SimpleDateFormat = SimpleDateFormat("a hh:mm")

    private lateinit var dataList: MutableList<Update>
    private lateinit var mContext: Context
    private lateinit var eventID: String

    constructor(context: Context) : this(){
        dataList = ArrayList()
        mContext = context

    }

    fun setData(dealData: List<Update>) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    private fun dealChatRoomPhoto(data: Update, imageView: ImageView){
        val jsonString = data.topic ?: "null"
        val jsonObject = JSONObject(jsonString)
        var url = ""

        if(jsonObject.get("type") == "event_group"){
            url = BuildConfig.CHATROOM_IMAGE_URL+"event/"+data.fname.replace("event_", "")+".jpg"
            eventID = data.fname.replace("event_", "")
        } else if(jsonObject.get("type") == "dating_group"){
            eventID = ""
            url = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+data.fname.replace("_"+PrefHelper.chatLable, "")+".jpg"
        }
        Log.e("Peter", "dealChatRoomPhoto   $url")

        ImgHelper.loadNormalImg(mContext, url, imageView)
    }

    private fun dealChatRoomName(data: Update): String{
        val jsonString = data.topic ?: "null"

        if ("null" == jsonString){
            return ""
        }
        Log.e("Peter", "jsonObject   $jsonString")
        Log.e("Peter", "jsonObject data._id  ${data._id}")

        val jsonObject = JSONObject(jsonString)

        return if (jsonObject.has(PrefHelper.chatLable)){

            jsonObject.getString(PrefHelper.chatLable)
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
        lateinit var ivChatRoomPhoto: ImageView
        lateinit var tvChatLastMsg: TextView
        lateinit var tvChatLastMsgTime: TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = LayoutInflater.from(mContext).inflate(R.layout.item_chat_room, parent, false)
        val viewHolder = ViewHolder(cell)
        viewHolder.tvChatRoomName = cell.findViewById(R.id.tv_chat_room_name)
        viewHolder.ivChatRoomPhoto = cell.findViewById(R.id.iv_chat_room_photo)
        viewHolder.tvChatLastMsg = cell.findViewById(R.id.tv_chat_last_msg)
        viewHolder.tvChatLastMsgTime = cell.findViewById(R.id.tv_chat_last_msg_time)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataList.size == 0) {
            return
        }
        val data = dataList[position]
        Log.e("Peter", "CHATADAPTER:   " + data.fname)
        Log.e("Peter", "CHATADAPTER:   " + data)

        dealChatRoomPhoto(data, holder.ivChatRoomPhoto)
        holder.tvChatRoomName.text = ""
        holder.tvChatRoomName.text = dealChatRoomName(data)
        holder.itemView.setOnClickListener {
            IntentHelper.gotoChatRoom(mContext, data._id, data.fname.replace("event_", ""))
        }

        if(data.lastMessage == null){
            holder.tvChatLastMsg.text = ""

            return
        }

        val username = if(data.lastMessage.u.username == PrefHelper.chatLable){
            "你"
        } else {
            if(data.lastMessage != null){
                data.lastMessage.u.name
            } else {
                ""
            }
        }

        if(data.lastMessage.attachments != null){
            if(data.lastMessage.attachments[0].audio_type != null){
                holder.tvChatLastMsg.text = username+"傳送了錄音。"
            } else if(data.lastMessage.attachments[0].image_type != null){
                holder.tvChatLastMsg.text = username+"傳送了照片。"

            }
        } else {
            var messageEntry = MessageEntry("","","","", data.lastMessage.msg)

            try {
                messageEntry = Gson().fromJson(data.lastMessage.msg, MessageEntry::class.java)
            } catch (e: Exception){

            }

            holder.tvChatLastMsg.text = username+":"+messageEntry.content

        }

        when {
            DateFormatter.isToday(data.lastMessage._updatedAt) -> {
                holder.tvChatLastMsgTime.text = customTodayFormat.format(data.lastMessage._updatedAt)
            }
            DateFormatter.isYesterday(data.lastMessage._updatedAt) -> {
                holder.tvChatLastMsgTime.text = "昨天"
            }
            else -> {
                holder.tvChatLastMsgTime.text = customDateFormat.format(data.lastMessage._updatedAt)
            }
        }
    }

}