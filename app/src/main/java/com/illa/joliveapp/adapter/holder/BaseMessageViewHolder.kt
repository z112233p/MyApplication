package com.illa.joliveapp.adapter.holder

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.Adapter_Chat_Room_Message
import com.illa.joliveapp.custom_view.AudioPlayerLayout
import com.illa.joliveapp.datamodle.chat.text_message.message_entry.MessageEntry
import com.illa.joliveapp.datamodle.chat_room.Message
import com.illa.joliveapp.tools.AudioRecordHelper
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.Tools
import com.google.gson.Gson
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.utils.DateFormatter
import java.io.File


abstract class BaseMessageViewHolder(itemView: View) :
    MessagesListAdapter.BaseMessageViewHolder<Message>(itemView),View.OnLongClickListener {
    val messageUserAvatar = itemView.findViewById<ImageView>(R.id.messageUserAvatar)
    val llMessageMain = itemView.findViewById<LinearLayout>(R.id.ll_message_main)
    val image = itemView.findViewById<ImageView>(R.id.image)
    val messageText = itemView.findViewById<TextView>(R.id.messageText)
    val messageTime = itemView.findViewById<TextView>(R.id.messageTime)
    val name = itemView.findViewById<TextView>(R.id.tv_name)

    val llAudioLayout = itemView.findViewById<AudioPlayerLayout>(R.id.ll_audio_layout)
    val parent = itemView

    val llMessageReply = itemView.findViewById<LinearLayout>(R.id.ll_message_reply)
    val ivReplyImage = itemView.findViewById<ImageView>(R.id.iv_reply_image)
    val tvReplyText = itemView.findViewById<TextView>(R.id.tv_reply_text)
    val vDividerLine = itemView.findViewById<View>(R.id.v_divider_line)
    val tvStatus = itemView.findViewById<TextView>(R.id.tv_status)

    var width = 0

    init {
//        parent.isLongClickable = true
//        messageText.setOnLongClickListener(this)

        val dm2: DisplayMetrics = MyApp.get()?.resources?.displayMetrics!!
        println("heigth2 : " + dm2.heightPixels)
        println("width2 : " + dm2.widthPixels)
        width = dm2.widthPixels

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onLongClick(p0: View?): Boolean {
        Log.e("peter", "onLongClickonLongClickonLongClick??:   "+width)

        Log.e("peter", "onLongClickonLongClickonLongClick:   "+messageUserAvatar.measuredWidth.toFloat())
//        parent.showContextMenu((messageUserAvatar.measuredWidth.toFloat())*2, -100F)
//        parent.showContextMenu((width.toFloat()-(messageUserAvatar.measuredWidth.toFloat())), -100F)
//        val popup = MyApp.get()?.applicationContext?.let { CardPopup(it) }
//
//        popup?.showOnAnchor(llMessageMain, RelativePopupWindow.VerticalPosition.ABOVE, RelativePopupWindow.HorizontalPosition.CENTER)

        return true
    }

    override fun onBind(message: Message) {
        llAudioLayout.initSeekBar()
        Log.e("Peter", "onBind.payload?     $payload")
        Log.e("Peter", "onBind.payload?     $")
        val payload2 :Adapter_Chat_Room_Message<Message> = Adapter_Chat_Room_Message()

        val payload = payload2.payload
        var messageEntry = MessageEntry("","","","", message.text)

        try {
            messageEntry = Gson().fromJson(message.text, MessageEntry::class.java)
        } catch (e: Exception){

        }

        val randomReplyLayoutBool = !TextUtils.isEmpty(messageEntry.retype)//nextBoolean()
        val randomReplyItemBool = messageEntry.retype == "img"//nextBoolean()

        //basic item

        Log.e("Peter","message.imageUrl:  randomReplyLayoutBool   "+randomReplyLayoutBool)
        Log.e("Peter","message.imageUrl:  randomReplyItemBool    "+randomReplyItemBool)
        Log.e("Peter","message.imageUrl:  name    "+message.author.name)
        Log.e("Peter","message.imageUrl:  img    "+message.imageUrl)
        Log.e("Peter","message.imageUrl:  reimg   "+messageEntry.recontent)

        messageText.visibility = View.GONE
        image.visibility = View.GONE
        llAudioLayout.visibility = View.GONE
//        voicePlayerView

        name.text = message.author.name

        if (TextUtils.isEmpty(message.getFileType())){
            messageText.text = messageEntry.content
            messageText.visibility = View.VISIBLE

        } else {
            if (message.getFileType() == "image/jpg"){
                if (!TextUtils.isEmpty(message.imageUrl)){
                    ImgHelper.loadOriginalImage(MyApp.get()?.applicationContext, message.imageUrl, this.image)
                    llMessageMain.background = null


                }
                image.visibility = View.VISIBLE

            } else if (message.getFileType() == "audio/m4a"){
                message.getAudioUrl()?.let {
                    Log.e("Peter","message.getAudioUrl:  audio    "+message.getAudioUrl())
                    llAudioLayout.setAudioSource(it)
//                    voicePlayerView.setAudio(it)
                    when {
                        it.contains("https://") -> {
                            Log.e("Peter","audioPlayer play")
                            Tools.downloadFile3(it, object : Tools.AudioDownListener{
                                override fun success() {
                                    Log.e("Peter","DOWNLOAD AUDIO success")
                                    llAudioLayout.setWaveSeekBar(File(Tools.getLocalSavedAudioPath()+it.substring(it.lastIndexOf("/") + 1)).path)

                                }

                            })

                        }
                        it.contains(Environment.getExternalStorageDirectory().toString()) -> {
                            Log.e("Peter","audioPlayer play1")

                        }
                        else -> {

                            AudioRecordHelper.playLocalRecord()

                        }
                    }

                }
                llAudioLayout.visibility = View.VISIBLE
            }
        }



        ImgHelper.loadNormalImg(MyApp.get()?.applicationContext, message.author.avatar, messageUserAvatar)
        messageTime.text = DateFormatter.format(message.createdAt, DateFormatter.Template.TIME)

        //reply item(TEST)

        if(randomReplyLayoutBool){
            if(randomReplyItemBool){
                ImgHelper.loadNormalImg(MyApp.get()?.applicationContext, messageEntry.recontent, ivReplyImage)
            } else {
                tvReplyText.text = messageEntry.recontent
            }
        }
        llMessageReply.visibility = if (randomReplyLayoutBool) View.VISIBLE else View.GONE
        vDividerLine.visibility = if (randomReplyLayoutBool) View.VISIBLE else View.GONE
        ivReplyImage.visibility = if (randomReplyItemBool) View.VISIBLE else View.GONE
        tvReplyText.visibility = if (randomReplyItemBool) View.GONE else View.VISIBLE

        messageUserAvatar?.setOnClickListener {
            payload?.avatarClickListener?.onAvatarClick(message, itemView)
        }

        llMessageReply?.setOnClickListener {
            payload?.replyClickListener?.onReplyClick()
        }
        image.setOnClickListener {
            payload?.imageClickListener?.onImageClick(message, itemView)
        }

        if (message.getSuccess() == "true"){
            tvStatus.text = "true"
        } else if (message.getSuccess() == "false"){
            tvStatus.text = "faile"

        }

        itemView.setOnClickListener {
            Log.e("peter", "itemView.setOnClickListener??"+  message.getFileType())
            if (message.getFileType() == "audio/m4a"){
                llAudioLayout.playAudio(message.getAudioUrl()!!)


            }
        }
    }

    private fun setClick(){
        image.setOnClickListener {
            payload
        }
    }


    //Item Onclick Interface
    interface OnAvatarClickListener {
        fun onAvatarClick(message: Message, itemView: View)
    }

    interface OnReplyClickListener {
        fun onReplyClick()
    }

    interface OnImageClickListener {
        fun onImageClick(message: Message, itemView: View)
    }

    class Payload {
        var avatarClickListener: OnAvatarClickListener? = object : OnAvatarClickListener{
            override fun onAvatarClick(message: Message, itemView: View) {
                Log.e("BaseMessageViewHolder","onAvatarClick")
            }
        }
        var replyClickListener: OnReplyClickListener? = object : OnReplyClickListener{
            override fun onReplyClick() {
                Log.e("BaseMessageViewHolder","onReplyClick")
            }
        }
        var imageClickListener: OnImageClickListener? = object : OnImageClickListener{
            override fun onImageClick(message: Message, itemView: View) {

                Log.e("BaseMessageViewHolder","onImageClick")
            }
        }

        fun setImgClickListener(iii: OnImageClickListener) {
            imageClickListener = iii
        }

        fun setAvatarOnClickListener(param: OnAvatarClickListener){
            avatarClickListener = param
        }

    }
}