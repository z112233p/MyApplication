package com.example.myapplication.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.`interface`.WebSocketModle
import com.example.myapplication.adapter.Adapter_Chat_Room_Message
import com.example.myapplication.adapter.holder.BaseMessageViewHolder
import com.example.myapplication.adapter.holder.CustomOutComingTextMessageViewHolder
import com.example.myapplication.custom_view.CardPopup
import com.example.myapplication.datamodle.chat.MessageReceived.message_received_args.MessageReceivedArgs2
import com.example.myapplication.datamodle.chat.text_message.TMessage
import com.example.myapplication.datamodle.chat.text_message.TextMessage
import com.example.myapplication.datamodle.chat.text_message.message_entry.MessageEntry
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.dialog.DialogFullScreenImage
import com.example.myapplication.network.WebSocketHelper
import com.example.myapplication.tools.AudioRecordHelper
import com.example.myapplication.tools.Config
import com.example.myapplication.tools.ImgHelper
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.viewmodle.ChatRoomActivityVM
import com.google.gson.Gson
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders.ContentChecker
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChatRoomActivity : AppCompatActivity(), WebSocketModle {

    private val chatRoomActVM: ChatRoomActivityVM by viewModel()
    private var updateOption: Boolean = true
    private var latest: Date? = null

    private lateinit var rId: String
    private lateinit var adapter: Adapter_Chat_Room_Message<Message>
    private lateinit var dataList: ArrayList<Message>
    private lateinit var imm: InputMethodManager
    private lateinit var popup: CardPopup
    private lateinit var replyMessageEntry: MessageEntry
    private lateinit var imgUrlMap:HashMap<String, String>


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }
        imgUrlMap = HashMap<String,String>()
        dataList = ArrayList()
        getIntentData()
        init()
        initObserve()
        chatRoomActVM.getChatHistory(rId)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        btn_start_record.setOnClickListener {
            AudioRecordHelper.startRecord()
        }

        btn_stop_record.setOnClickListener {
            val file = File(AudioRecordHelper.stopRecord())
            val audioData = Message()
            val messageID = java.util.UUID.randomUUID().toString()

            audioData.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+PrefHelper.getChatLable()+".jpg"
            audioData.setImageUrl(AudioRecordHelper.stopRecord())
            audioData.id = messageID
            audioData.setFileType("audio/m4a")
            audioData.setAudioUrl(AudioRecordHelper.stopRecord())

            adapter.addToStart(audioData,true)
            chatRoomActVM.postAudioMessage(file, rId, audioData)

        }

        btn_play_record.setOnClickListener {
            AudioRecordHelper.playLocalRecord()
        }
        fab_click_to_bottom.setOnClickListener {
            messagesList.scrollToPosition(0)
            fab_click_to_bottom.visibility = View.GONE
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            Log.e("Peter","onTouchEvent")
            if(currentFocus !=null && currentFocus?.windowToken !=null){
                imm.hideSoftInputFromWindow(currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getIntentData(){
        rId = ""
        val b = intent?.extras
        rId = b?.getString("ChatRoomID")!!
        PrefHelper.setChatRoomId(rId)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility", "WrongConstant")
    private fun init(){
        AudioRecordHelper.setContext(this)
        replyMessageEntry = MessageEntry("","","","","")
        //set Socket Option
        WebSocketHelper.setCallBack(this)
        WebSocketHelper.setAct(this)

        //init MessagesListAdapter
        val imageLoader: ImageLoader =
            ImageLoader { imageView, url, payLoad ->
                //Picasso.with(this@ChatRoomActivity).load(url).into(imageView)
                ImgHelper.loadNormalImg(this@ChatRoomActivity, url, imageView)
            }

        val test = ContentChecker<Message> { message, type ->
            Log.e("ss","ss")
            false
        }
        
        val payload = BaseMessageViewHolder.Payload()
        val iii = object : BaseMessageViewHolder.OnImageClickListener{
            override fun onImageClick(message: Message) {
                val dialog = DialogFullScreenImage(this@ChatRoomActivity, message)
                Log.e("Peter","onImageClick OMG LIL")
                dialog.show()
            }
        }
        payload.setAvatarClickListener(iii)

        val holdersConfig = MessagesListAdapter.HoldersConfig()
//        holdersConfig.setOutcoming(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message)
//        holdersConfig.setIncoming(CustomOutComingTextMessageViewHolder::class.java,R.layout.itemc_custom_incoming_message)
        holdersConfig.setOutcomingImageConfig(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message )
        holdersConfig.setIncomingImageConfig(CustomOutComingTextMessageViewHolder::class.java,R.layout.item_custom_incoming_message)
        holdersConfig.setOutcomingTextConfig(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message)
        holdersConfig.setIncomingTextConfig(CustomOutComingTextMessageViewHolder::class.java,R.layout.item_custom_incoming_message)

//        holdersConfig.setOutcomingLayout(R.layout.item_custom_outcoming_message)
//        holdersConfig.registerContentType(
//            1,
//            OutcomingVoiceMessageViewHolder::class.java,
//            R.layout.item_custom_outcoming_message,
//            OutcomingVoiceMessageViewHolder::class.java,
//            R.layout.item_custom_outcoming_message,
//            test)

        adapter = Adapter_Chat_Room_Message(PrefHelper.getChatLable(), holdersConfig, imageLoader)
        adapter.payload = payload
        adapter.setOnMessageViewClickListener { view, message ->
            imm.hideSoftInputFromWindow(currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
        }

        adapter.setOnMessageViewLongClickListener { view, message ->
            Log.e("Peter", "setOnMessageViewLongClickListener:  "+(message as Message).author.name)
            Log.e("Peter", "setOnMessageViewLongClickListener:  "+message.author.avatar)
            Log.e("Peter", "setOnMessageViewLongClickListener:  "+message.imageUrl)
            Log.e("Peter", "setOnMessageViewLongClickListener:  "+message.text)


            val viewLocation = IntArray(2)
            view.getLocationInWindow(viewLocation)
            val viewY = viewLocation [1]
            Log.e("Peter", "viewLocation   y:   $viewY")
            if(viewY > 150){
                popup = CardPopup(this, false, message)
                popup.showOnAnchor(view.findViewById(R.id.ll_message_main), RelativePopupWindow.VerticalPosition.ABOVE,
                    RelativePopupWindow.HorizontalPosition.CENTER)

            } else {
                popup = CardPopup(this, true, message)
                popup.showOnAnchor(view.findViewById(R.id.ll_message_main), RelativePopupWindow.VerticalPosition.BELOW,
                    RelativePopupWindow.HorizontalPosition.CENTER)
            }
            //CardPopup
            popup.setCopyButtonListener(object : CardPopup.onCopyButtonClickListener{
                override fun onCopyButtonClick(message: Message) {
                    Log.e("Peter","onCopyButtonClick ININDER  "+message.text)
                    var messageEntry = MessageEntry("","","","",message.text)

                    try {
                        messageEntry = Gson().fromJson(message.text, MessageEntry::class.java)
                    } catch (e: Exception){
                        Log.e("Peter","onCopyButtonClick messageEntry   err  "+e)

                    }
                    Log.e("Peter","onCopyButtonClick messageEntry1  "+message.imageUrl)
                    Log.e("Peter","onCopyButtonClick messageEntry2  "+messageEntry.content)
                    Log.e("Peter","onCopyButtonClick messageEntry3  "+messageEntry.recontent)
                    Log.e("Peter","onCopyButtonClick messageEntry4  "+messageEntry.relabel)
                    Log.e("Peter","onCopyButtonClick messageEntry5  "+messageEntry.retype)
                    Log.e("Peter","onCopyButtonClick messageEntry  ava"+message.author.avatar)
                    replyMessageEntry.recontent = messageEntry.content
                    replyMessageEntry.rename = message.author.name
                    replyMessageEntry.relabel = message.author.id
                    replyMessageEntry.retype = "text"


                    ImgHelper.loadNormalImg(this@ChatRoomActivity,message.author.avatar,img_owner)
                    if (!TextUtils.isEmpty(message.imageUrl)){
                        Log.e("Peter","onCopyButtonClick messageEntry  sEmpty(message.imageUrl)   1   "+imgUrlMap[message.id].toString())
                        replyMessageEntry.retype = "img"

                        if (imgUrlMap.containsKey(message.id)){
                            replyMessageEntry.recontent = imgUrlMap[message.id].toString()
                            ImgHelper.loadNormalImg(this@ChatRoomActivity,imgUrlMap[message.id].toString(),iv_reply_image2)
                        } else {
                            replyMessageEntry.recontent = message.imageUrl.toString()
                            ImgHelper.loadNormalImg(this@ChatRoomActivity,message.imageUrl,iv_reply_image2)
                        }

                        iv_reply_image2.visibility = View.VISIBLE
                    } else {
                        Log.e("Peter","onCopyButtonClick messageEntry  sEmpty(message.imageUrl)   2")
                        iv_reply_image2.visibility = View.GONE

                    }
                    tv_reply_content.text = messageEntry.content
                    tv_reply_name.text = message.author.name
                    ll_reply_layout.visibility = View.VISIBLE
                    popup.dismiss()
                    input.inputEditText.requestFocus()
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            })
        }

//        adapter.onLoadMore()

        //init messagesList(Recycle View)
        messagesList.setAdapter(adapter)
        messagesList.setOnTouchListener { view, motionEvent ->
            if(motionEvent?.action == MotionEvent.ACTION_DOWN){
                Log.e("Peter","onTouchEvent")
                if(currentFocus !=null && currentFocus?.windowToken != null){
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        messagesList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e("=====newState", "" + newState)

                if (recyclerView.childCount > 0) {
                    try {
                        val currentPosition =(recyclerView.getChildAt (0).layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
                        Log.e("=====currentPosition", "" + currentPosition)
                        var b = recyclerView.canScrollVertically(-1)
                        Log.e("=====canScroll", "" + b)
                        if (!b && !updateOption){
                            updateOption = true
                            latest?.let { chatRoomActVM.getChatHistory(rId, it) }
                        }
                        if (currentPosition == 0){
                            fab_click_to_bottom.visibility = View.GONE
                        } else {
                            fab_click_to_bottom.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                    }
                }

            }
        } )

        //init input (editView)
        input.setInputListener {
            Log.e("Peter", "setInputListener    $it")
            val data = Message()
            val messageID = java.util.UUID.randomUUID().toString()
            var tMessage = TMessage(messageID, it.toString(), rId)

//            val messageEntry = MessageEntry("", "","",it.toString())
            if (TextUtils.isEmpty(replyMessageEntry.relabel)){
                data.text = it.toString()
            } else {
                replyMessageEntry.content = it.toString()
                data.text = Gson().toJson(replyMessageEntry)
                tMessage = TMessage(java.util.UUID.randomUUID().toString(),Gson().toJson(replyMessageEntry),rId)
            }

            data.id = messageID
            data.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+PrefHelper.getChatLable()+".jpg"
            val dataBody = TextMessage(tMessage)
            adapter.addToStart(data,true)
            Log.e("Peter", "onMessageT  messageID:  $messageID")

            chatRoomActVM.postTextMessage(dataBody, rId, data)

            iv_close.performClick()
            return@setInputListener true
        }

        input.setAttachmentsListener {
            CropImage.activity().start(this)
        }

        iv_close.setOnClickListener {
            replyMessageEntry.clear()
            ll_reply_layout.visibility = View.GONE
        }
    }

    private fun initObserve(){
        chatRoomActVM.getMessageUpdate().observe(this, Observer {
            it.imageUrl?.let { it1 -> imgUrlMap.put(it.id, it1) }
            adapter.update(it, false)
        })

        chatRoomActVM.getChatHistoryData().observe(this, Observer { list  ->
            if (list.size > 0){
                latest = list[list.size -1]._updatedAt
            } else {
                latest = null

            }
            dataList.clear()
                list.forEach { it ->
                if(!TextUtils.isEmpty(it.u.name)){
                    val data = Message()
                    data.author.name = it.u.name
                    data.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+it.u.username+".jpg"
                    data.createdAt = it._updatedAt
                    data.author.id = it.u.username
                    data.text = it.msg
                    if(it.attachments != null){
                        val glideUrl = GlideUrl(BuildConfig.CHATROOM_URL+ it.attachments[0].image_url,
                            LazyHeaders.Builder()
                                .addHeader("X-Auth-Token",PrefHelper.getChatToken())
                                .addHeader("X-User-Id",PrefHelper.getChatId())
                                .build())
                        data.setImageUrl(glideUrl.toString())
                        data.setAudioUrl(BuildConfig.CHATROOM_URL+ it.attachments[0].title_link)
                        Log.e("Peter","CHATIMAGE:   "+glideUrl.toString())
                    }
                    if (it.file != null){
                        it.file.type?.let { it1 -> data.setFileType(it1) }
                    }
                    dataList.add(0 ,data)
                }
            }
            if (updateOption){
                adapter.addToEnd(dataList,true)

//                dataList.forEach{
//                    adapter.addToStart(it,true)
//                }
            } else {
                adapter.addToEnd(dataList,true)
            }

            updateOption = false

        })
    }

    override fun messageReceive(dataList: MessageReceivedArgs2) {
        val data = Message()
        data.author.id = dataList.lastMessage.u.username
        data.author.name = dataList.lastMessage.u.name
        data.text = dataList.lastMessage.msg
        data.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+dataList.lastMessage.u.username+".jpg"

        if (dataList.lastMessage.file != null){
            dataList.lastMessage.file.type?.let { data.setFileType(it) }
            when(dataList.lastMessage.file.type){
                Config.IMAGE_JPG -> data.setImageUrl(BuildConfig.CHATROOM_URL+ dataList.lastMessage.attachments[0].image_url)
                Config.AUDIO_M4A -> data.setAudioUrl(BuildConfig.CHATROOM_URL+ dataList.lastMessage.attachments[0].title_link)
            }
        }

        adapter.addToStart(data,true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK){
                val result = CropImage.getActivityResult(data)
                val file = File(result.uri.path)
                val imgData = Message()
                val messageID = java.util.UUID.randomUUID().toString()

                imgData.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+PrefHelper.getChatLable()+".jpg"
                imgData.setImageUrl(result.uri.toString())
                imgData.id = messageID
                imgData.setFileType("image/jpg")
                adapter.addToStart(imgData,true)
                chatRoomActVM.postImageMessage(file, rId, imgData)
            }
        }
    }

}

