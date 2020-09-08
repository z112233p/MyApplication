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
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.`interface`.WebSocketModle
import com.example.myapplication.adapter.Adapter_Chat_Room_Message
import com.example.myapplication.adapter.holder.CustomOutComingTextMessageViewHolder
import com.example.myapplication.custom_view.CardPopup
import com.example.myapplication.datamodle.chat.MessageReceived.message_received_args.MessageReceivedArgs2
import com.example.myapplication.datamodle.chat.text_message.TMessage
import com.example.myapplication.datamodle.chat.text_message.TextMessage
import com.example.myapplication.datamodle.chat.text_message.message_entry.MessageEntry
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.network.WebSocketHelper
import com.example.myapplication.tools.AudioRecordHelper
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
import kotlinx.android.synthetic.main.item_custom_outcoming_message.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChatRoomActivity : AppCompatActivity(), WebSocketModle {

    private val chatRoomActVM: ChatRoomActivityVM by viewModel()

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



//        btn_start_record.setOnTouchListener { view, motionEvent ->
//            when(motionEvent.action){
//                MotionEvent.ACTION_DOWN ->{
//                    AudioRecordHelper.startRecord()
//                    AudioRecordHelper.countTime()
//                }
//                MotionEvent.ACTION_UP ->{
//                    AudioRecordHelper.stopRecord()
//
//                }
//            }
//
//            return@setOnTouchListener false
//        }

        btn_start_record.setOnClickListener {
            AudioRecordHelper.startRecord()
//            AudioRecordHelper.countTime()
        }
        btn_stop_record.setOnClickListener {
            AudioRecordHelper.stopRecord()
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

        val holdersConfig = MessagesListAdapter.HoldersConfig()
        holdersConfig.setOutcoming(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message)
        holdersConfig.setIncoming(CustomOutComingTextMessageViewHolder::class.java,R.layout.item_custom_incoming_message)
        holdersConfig.setOutcomingImageConfig(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message)
        holdersConfig.setIncomingImageConfig(CustomOutComingTextMessageViewHolder::class.java,R.layout.item_custom_incoming_message)


//        holdersConfig.setOutcomingLayout(R.layout.item_custom_outcoming_message)
//        holdersConfig.registerContentType(
//            1,
//            OutcomingVoiceMessageViewHolder::class.java,
//            R.layout.item_custom_outcoming_message,
//            OutcomingVoiceMessageViewHolder::class.java,
//            R.layout.item_custom_outcoming_message,
//            test)

        adapter = Adapter_Chat_Room_Message<Message>(PrefHelper.getChatLable(), holdersConfig, imageLoader)

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
            CropImage.activity().setAspectRatio(250,250).start(this)
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
                        Log.e("Peter","CHATIMAGE:   "+glideUrl.toString())
                    }
                    dataList.add(0,data)
                }
            }
            dataList.forEach{
                adapter.addToStart(it,true)
            }
        })
    }

    override fun messageReceive(dataList: MessageReceivedArgs2) {
//        Log.e("peter", "messageReceive   IN${dataList.lastMessage.u.username}")

        val data = Message()
        data.author.id = dataList.lastMessage.u.username
        data.author.name = dataList.lastMessage.u.name
        data.text = dataList.lastMessage.msg
        data.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+dataList.lastMessage.u.username+".jpg"


        if(dataList.lastMessage.attachments != null){
            Log.e("Peter","messageReceive:  img    "+dataList.lastMessage.attachments[0].image_url)

            data.setImageUrl(BuildConfig.CHATROOM_URL+ dataList.lastMessage.attachments[0].image_url)
            Log.e("Peter","messageReceive:  data.setImageUrl    "+data.imageUrl)

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

                adapter.addToStart(imgData,true)
                chatRoomActVM.postImageMessage(file, rId, imgData)
            }
        }
    }

}

