package com.illa.joliveapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.`interface`.WebSocketModle
import com.illa.joliveapp.adapter.Adapter_Chat_Room_Message
import com.illa.joliveapp.adapter.holder.BaseMessageViewHolder
import com.illa.joliveapp.adapter.holder.CustomInComingTextMessageViewHolder
import com.illa.joliveapp.adapter.holder.CustomOutComingTextMessageViewHolder
import com.illa.joliveapp.custom_view.*
import com.illa.joliveapp.datamodle.chat.MessageReceived.message_received_args.MessageReceivedArgs2
import com.illa.joliveapp.datamodle.chat.text_message.TMessage
import com.illa.joliveapp.datamodle.chat.text_message.TextMessage
import com.illa.joliveapp.datamodle.chat.text_message.message_entry.MessageEntry
import com.illa.joliveapp.datamodle.chat_room.Message
import com.illa.joliveapp.dialog.DialogChatRoomMenu
import com.illa.joliveapp.network.WebSocketHelper
import com.illa.joliveapp.tools.*
import com.illa.joliveapp.viewmodle.ChatRoomActivityVM
import com.google.gson.Gson
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders.ContentChecker
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.utils.DateFormatter
import com.theartofdev.edmodo.cropper.CropImage
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_chat_room.toolbar
import kotlinx.android.synthetic.main.activity_chat_room_v3.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class ChatRoomActivity : AppCompatActivity(), WebSocketModle {

    private val chatRoomActVM: ChatRoomActivityVM by viewModel()
    private var updateOption: Boolean = true
    private var latest: Date? = null
    private var customDateFormat: SimpleDateFormat = SimpleDateFormat("MM月dd日 E")
    private lateinit var dialog :DialogChatRoomMenu
    private lateinit var optionLayout: LinearLayout
    private lateinit var animLayout: MenuItemLayout3
    private lateinit var backgroundCardViewHolder: CardView
    private lateinit var personalDataLayout: PersonalDataLayout
    private lateinit var eventDataLayout: EventDataLayout

    private var layoutHeight by Delegates.notNull<Int>()
    private var hasAnimation: Boolean = false
    private lateinit var mBlurringView: BlurringView
    private lateinit var mImageView: ImageView
    private lateinit var transactionBitmap: Bitmap

    private lateinit var rId: String
    private lateinit var eventId: String
    private var eventLabel = ""

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
        setContentView(R.layout.activity_chat_room_v3)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
//            val intent = Intent(this, MapsActivity::class.java)
//            startActivity(intent)
        }
        mBlurringView = BlurringView(this)
        title = ""
        tv_toolbar_title.text = "聊天室"

        mImageView = ImageView(this)
        mImageView.setImageDrawable(this.resources.getDrawable(R.drawable.ic_send))
        val drawable :BitmapDrawable = mImageView.drawable as BitmapDrawable
        transactionBitmap = drawable.bitmap

        animLayout = MenuItemLayout3(this)
        animLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,1500)
        animLayout.setBackgroundColor(this.resources.getColor(R.color.transparent))
        animLayout.background = null

        layoutHeight = 1500
        backgroundCardViewHolder = CardView(this)
        backgroundCardViewHolder.background = this.resources.getDrawable(R.drawable.bg_anima_view)
        backgroundCardViewHolder.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
        backgroundCardViewHolder.alpha = 0.5F
        personalDataLayout = PersonalDataLayout(this)

        personalDataLayout.post {
            Log.e("Peter","personalDataLayout.post    ${personalDataLayout.measuredHeight}")
            Log.e("Peter","personalDataLayout.post2    ${personalDataLayout.height}")

//            layoutHeight = personalDataLayout.height + 250
//            optionLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,layoutHeight)
//            animLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,layoutHeight)
//            layoutHeight -= 250
        }

        eventDataLayout = EventDataLayout(this)

        eventDataLayout.post {
            Log.e("Peter","eventDataLayout.post    ${personalDataLayout.measuredHeight}")
            Log.e("Peter","eventDataLayout.post2    ${personalDataLayout.height}")

//            layoutHeight = eventDataLayout.height + 250
//            optionLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,layoutHeight)
//            animLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,layoutHeight)
//            layoutHeight -= 250
        }



        optionLayout = LinearLayout(this)
//        optionLayout.alpha = 0.5F


        optionLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1500)
        optionLayout.orientation = LinearLayout.VERTICAL
        optionLayout.setBackgroundColor(this.resources.getColor(R.color.transparent))
//        optionLayout.addView(personalDataLayout)
        optionLayout.addView(eventDataLayout)
        optionLayout.addView(personalDataLayout)

        val itemNames = this.resources.getStringArray(R.array.chat_room_menu)
//        itemNames.forEach { name ->
//            optionLayout.addView(MenuItemLayout(this).apply {
//                setIcon(R.drawable.ic_send)
//                setItemName(name)
//            })
//        }

//        animLayout.post {
//            animLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,layoutHeight)
//        }

        optionLayout.post {
            layoutHeight = optionLayout[0].height * (optionLayout.childCount) + 250
            optionLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,layoutHeight)
            animLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,layoutHeight)
            layoutHeight -= 250
        }

        animLayout.setCallBack(object : MenuItemLayout3.AnimListener{
            override fun closeView(upMoveTotal: Int) {
                animLayout.startAnimation(AnimHelper.getVerticalAnimation(animLayout, 200,
                            hasAnimation, layoutHeight, layoutHeight - upMoveTotal))
                        hasAnimation = !hasAnimation
                ll_gray_view.visibility = View.GONE

            }

            override fun setAnimAlpha(offY: Int) {
                var alphaParam: Float = ll_gray_view.alpha
                Log.e("Peter","setAnimAlpha0   $alphaParam")
                alphaParam -= 0.5F * offY / layoutHeight
                Log.e("Peter","setAnimAlpha   $alphaParam")
                ll_gray_view.alpha = alphaParam
            }
        })
        val display: Display = this.windowManager.defaultDisplay

        // 获取屏幕宽和高
        val widths = display.width

        mImageView.layoutParams= ViewGroup.LayoutParams(widths,ViewGroup.LayoutParams.WRAP_CONTENT)

        animLayout.addView(backgroundCardViewHolder)
        animLayout.addView(mImageView)
        animLayout.addView(optionLayout)
//        animLayout.addView(personalDataLayout)
        ll_chat_room_main.addView(animLayout)


        animLayout.viewTreeObserver.addOnPreDrawListener {
            val location = IntArray(2)
            Log.e("Peter","animLayout.viewTreeObserver    ${animLayout.getLocationInWindow(location)}")
            Log.e("Peter","animLayout.viewTreeObserver0    ${location[1]}")

//
            val w = transactionBitmap.width // 得到图片的宽，高
            val h = transactionBitmap.height
//
            val display: Display = this.windowManager.defaultDisplay

            // 获取屏幕宽和高
            val heights = display.height
//            Log.e("Peter","animLayout.viewTreeObserver2  bitmap   $heights   $h")

            var fix = 0
            if(heights > h){
                fix = heights - h
                fix++
            }
            var animHeight = h +fix -location[1]
            Log.e("Peter","animLayout.viewTreeObserver2  bitmap   $h    $heights   $animHeight")

            if((h +fix -location[1]) <= 0){
                animHeight = 1
            }
            Log.e("Peter","animLayout.viewTreeObserver4  bitmap   ${location[1]}    $h    $heights  ")

            Log.e("Peter","animLayout.viewTreeObserver3  bitmap   ${location[1] -fix}    $animHeight ")

            if(location[1] -fix + animHeight <= h && (location[1] -fix) >= 0){
                val bitmap2 = Bitmap.createBitmap(transactionBitmap, 0, location[1] -fix, w, animHeight, null, true);
                mImageView.setImageBitmap(bitmap2)
            }

            true
        }


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
        if(!TextUtils.isEmpty(eventId)){
            chatRoomActVM.getEventDetailById(eventId)
        }
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager


        btn_start_record.setOnClickListener {
            AudioRecordHelper.startRecord()
        }


        btn_stop_record.setOnClickListener {
            val file = File(AudioRecordHelper.stopRecord())
            val audioData = Message()
            val messageID = java.util.UUID.randomUUID().toString()

            audioData.author.avatar = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg"
            audioData.setImageUrl(AudioRecordHelper.stopRecord())
            audioData.id = messageID
            audioData.setFileType("audio/m4a")
            audioData.setAudioUrl(AudioRecordHelper.stopRecord())

            adapter.addToStart(audioData,true)
            chatRoomActVM.postAudioMessage(file, rId, audioData)
            fl_audio_layout.visibility = View.GONE


        }

        btn_play_record.setOnClickListener {
            AudioRecordHelper.playLocalRecord()
        }
        fab_click_to_bottom.setOnClickListener {
            messagesList.scrollToPosition(0)
            fab_click_to_bottom.visibility = View.GONE
        }

        ll_gray_view.setOnClickListener {

            if(hasAnimation == false){
                return@setOnClickListener
            }
            Log.e("Peter","ll_gray_view.setOnClickList0    $hasAnimation")
            var alphaParam : Float = 0F
            var alphaParam2 : Float = 0.5F

            object : CountDownTimer(500, 25) {
                var option = hasAnimation
                override fun onFinish() {
                    if(hasAnimation == false){
                        ll_gray_view.visibility = View.GONE
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    if(hasAnimation == false){
                        alphaParam2 -= 0.025F
                        ll_gray_view.alpha = alphaParam2

                    } else {
                        alphaParam += 0.025F
                        ll_gray_view.alpha = alphaParam
                    }

                }
            }.start()

            Log.e("Peter","ll_gray_view.setOnClickList0    $hasAnimation")
            if(hasAnimation == true){
                animLayout.startAnimation(AnimHelper.getVerticalAnimation(animLayout, 500, hasAnimation,
                    layoutHeight, layoutHeight))
                hasAnimation = !hasAnimation
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("Peter","onRequestPermissionsResult")


    }

    private fun getIntentData(){
        rId = ""
        eventId = ""
        val b = intent?.extras
        rId = b?.getString("ChatRoomID")!!
        eventId = b.getString("eventID")!!
        PrefHelper.setChatRoomId(rId)
        Log.e("Peter","chatroom ID    $rId")
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
            override fun onImageClick(message: Message, itemView: View) {
//                val dialog = DialogFullScreenImage(this@ChatRoomActivity, message)
//                Log.e("Peter","onImageClick OMG LIL")
//                dialog.show()
//
                IntentHelper.gotoFullScreenImageActivity(this@ChatRoomActivity,
                    message.imageUrl!!, itemView.findViewById(R.id.image))
            }
        }
        payload.setImgClickListener(iii)
        payload.setAvatarOnClickListener(object : BaseMessageViewHolder.OnAvatarClickListener{
            override fun onAvatarClick(message: Message, itemView: View) {
                if(Tools.isFastDoubleClick()){
                    return
                }
                Log.e("Peter","onAvatarClick  message    ${message.author.id}")
                Log.e("Peter","onAvatarClick  message    ${message.author.name}")

                chatRoomActVM.getUserInfo(message.author.id)
                initAnimaView()
                personalDataLayout.setLabel(message.author.id)
                layoutHeight = personalDataLayout.height + 250
                optionLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,layoutHeight)
                animLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,layoutHeight)
                layoutHeight -= 250

                optionLayout.removeAllViews()
                optionLayout.addView(personalDataLayout)

                Log.e("Peter","item.action_member  layoutHeight    $layoutHeight")
                animLayout.startAnimation(AnimHelper.getVerticalAnimation(animLayout, 500,
                    hasAnimation, layoutHeight, layoutHeight))
                hasAnimation = !hasAnimation
            }

        })


        val holdersConfig = MessagesListAdapter.HoldersConfig()
//        holdersConfig.setOutcoming(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message)
//        holdersConfig.setIncoming(CustomOutComingTextMessageViewHolder::class.java,R.layout.itemc_custom_incoming_message)
        holdersConfig.setOutcomingImageConfig(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message_v2)
        holdersConfig.setIncomingImageConfig(CustomInComingTextMessageViewHolder::class.java,R.layout.item_custom_incoming_message_v2)
        holdersConfig.setOutcomingTextConfig(CustomOutComingTextMessageViewHolder::class.java, R.layout.item_custom_outcoming_message_v2)
        holdersConfig.setIncomingTextConfig(CustomInComingTextMessageViewHolder::class.java,R.layout.item_custom_incoming_message_v2)
//        holdersConfig.setIncomingTextConfig(CustomOutComingTextMessageViewHolder::class.java,R.layout.item_incoming_test)
//        holdersConfig.setIncomingLayout(R.layout.item_incoming_test)
//        holdersConfig.setDateHeaderHolder(CustomDateHolder::class.java)

//        holdersConfig.setOutcomingLayout(R.layout.item_custom_outcoming_message)
//        holdersConfig.registerContentType(
//            1,
//            OutcomingVoiceMessageViewHolder::class.java,
//            R.layout.item_custom_outcoming_message,
//            OutcomingVoiceMessageViewHolder::class.java,
//            R.layout.item_custom_outcoming_message,
//            test)

        adapter = Adapter_Chat_Room_Message(PrefHelper.chatLable, holdersConfig, imageLoader)
        adapter.payload = payload
        adapter.setDateHeadersFormatter {
            if (DateFormatter.isToday(it)) {
                "今天"
            } else if (DateFormatter.isYesterday(it)) {
                "昨天"
            } else {
                customDateFormat.format(it)
            }
        }
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
//                    input.inputEditText.requestFocus()
                    ed_send_msg.requestFocus()
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            })
        }
        messagesList.setAdapter(adapter)
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
        var i = 0

        val onGlobalLayoutListener =  object :ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                i++

                messagesList.scrollToPosition(0)
                Log.e("Peter", "onGlobalLayout    ")
                if(i>2){
                    messagesList.viewTreeObserver.removeOnGlobalLayoutListener(this)

                }
            }

        }

        //init input (editView)
//        input.setInputListener {
//            Log.e("Peter", "setInputListener    $it")
//            val data = Message()
//            val messageID = java.util.UUID.randomUUID().toString()
//            var tMessage = TMessage(messageID, it.toString(), rId)
//
////            val messageEntry = MessageEntry("", "","",it.toString())
//            if (TextUtils.isEmpty(replyMessageEntry.relabel)){
//                data.text = it.toString()
//            } else {
//                replyMessageEntry.content = it.toString()
//                data.text = Gson().toJson(replyMessageEntry)
//                tMessage = TMessage(java.util.UUID.randomUUID().toString(),Gson().toJson(replyMessageEntry),rId)
//            }
//
//            data.id = messageID
//            data.author.avatar = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg"
//            val dataBody = TextMessage(tMessage)
//            adapter.addToStart(data,true)
//            Log.e("Peter", "onMessageT  messageID:  $messageID")
//
//            chatRoomActVM.postTextMessage(dataBody, rId, data)
//
//            iv_close.performClick()
//            return@setInputListener true
//        }

        iv_send_photo.setOnClickListener {
            CropImage.activity().start(this)

        }

        iv_send_audio.setOnClickListener {
            fl_audio_layout.visibility = View.VISIBLE
        }

        iv_close_audio_view.setOnClickListener {
            fl_audio_layout.visibility = View.GONE

        }

        iv_close.setOnClickListener {
            replyMessageEntry.clear()
            ll_reply_layout.visibility = View.GONE
        }

        ed_send_msg.addTextChangedListener {
            Log.e("Peter","_send_msg.addTextChangedListene   $it")
            if(TextUtils.isEmpty(it)){
                tv_send_msg.linksClickable = false
                tv_send_msg.setTextColor(this.resources.getColor(R.color.colorNavBtn))

            } else{
                tv_send_msg.linksClickable = true
                tv_send_msg.setTextColor(Color.WHITE)
            }
        }

        tv_send_msg.setOnClickListener { view ->
            val it = ed_send_msg.text
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
            data.author.avatar = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg"
            val dataBody = TextMessage(tMessage)
            adapter.addToStart(data,true)
            Log.e("Peter", "onMessageT  messageID:  $messageID")

            chatRoomActVM.postTextMessage(dataBody, rId, data)

            iv_close.performClick()
            ed_send_msg.setText("")
        }

    }

    private fun initObserve(){
        chatRoomActVM.getEventDetailData().observe(this, Observer {
            Log.e("Peter","getEventDetailData INCHAT  $it")
            eventDataLayout.setData(it)
            eventLabel = it.data.label
        })


        chatRoomActVM.getUserInfoData().observe(this, Observer {
            personalDataLayout.setData(it)
        })

        chatRoomActVM.getMessageUpdate().observe(this, Observer {
            it.imageUrl?.let { it1 -> imgUrlMap.put(it.id, it1) }
            adapter.update(it, false)
            messagesList.scrollToPosition(0)
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
                    data.author.avatar = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+it.u.username+".jpg"
                    data.createdAt = it._updatedAt
                    data.author.id = it.u.username
                    data.text = it.msg
                    if(it.attachments != null){
                        val glideUrl = GlideUrl(BuildConfig.CHATROOM_URL+ it.attachments[0].image_url,
                            LazyHeaders.Builder()
                                .addHeader("X-Auth-Token", PrefHelper.chatToken!!)
                                .addHeader("X-User-Id", PrefHelper.chatId!!)
                                .build())
                        data.setImageUrl(glideUrl.toString())
                        data.setAudioUrl(BuildConfig.CHATROOM_URL+ it.attachments[0].title_link)
                        data.setFileDownload(BuildConfig.CHATROOM_URL+ it.attachments[0].title_link)

                        Log.e("Peter","title_linkDOWNLOAD:   "+BuildConfig.CHATROOM_URL+ it.attachments[0].title_link)

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

    override fun onResume() {
//        messagesList.scrollToPosition(0)

        super.onResume()
    }

    override fun messageReceive(dataList: MessageReceivedArgs2) {
        val data = Message()
        data.author.id = dataList.lastMessage.u.username
        data.author.name = dataList.lastMessage.u.name
        data.text = dataList.lastMessage.msg
        data.author.avatar = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+dataList.lastMessage.u.username+".jpg"

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
                Tools.saveCropImage(result.uri)
                val file = Tools.dealCropImage()
                val imgData = Message()
                val messageID = java.util.UUID.randomUUID().toString()

                Tools.deleteCropImage()
                imgData.author.avatar = BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg"
                imgData.setImageUrl(result.uri.toString())
                imgData.id = messageID
                imgData.setFileType("image/jpg")
                adapter.addToStart(imgData,true)
                chatRoomActVM.postImageMessage(file, rId, imgData)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_room, menu)

        return true
    }

    private fun initAnimaView(){
        messagesList.stopScroll()
        Blurry.with(this).radius(100).capture(ll_chat_room_main).into(mImageView)

        val drawable :BitmapDrawable = mImageView.drawable as BitmapDrawable
        transactionBitmap = drawable.bitmap
        val bitmap = drawable.bitmap
        mImageView.setImageBitmap(bitmap)

        val w = bitmap . getWidth () // 得到图片的宽，高
        val h = bitmap . getHeight ()
        Log.e("Peter","animLayout.viewTreeObserver2  bitmap   $w   $h")

        ll_gray_view.visibility = View.VISIBLE
        ll_gray_view.bringToFront()

        Log.e("Peter", "optionLayout    ${optionLayout.height}")

        var alphaParam : Float = 0F
        var alphaParam2 : Float = 0.5F

        object : CountDownTimer(500, 25) {
            override fun onFinish() {
                if(!hasAnimation){
                    ll_gray_view.visibility = View.GONE
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if(!hasAnimation){
                    alphaParam2 -= 0.025F
                    ll_gray_view.alpha = alphaParam2

                } else {
                    alphaParam += 0.025F
                    ll_gray_view.alpha = alphaParam
                }

            }
        }.start()
    }

    @SuppressLint("Range")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_option -> {
                if(Tools.isFastDoubleClick()){
                    return true
                }

                initAnimaView()

                layoutHeight = eventDataLayout.height + 250
                optionLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,layoutHeight)
                animLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,layoutHeight)
                layoutHeight -= 250

                optionLayout.removeAllViews()
                optionLayout.addView(eventDataLayout)

                Log.e("Peter","item.action_option  layoutHeight    $layoutHeight")
                animLayout.startAnimation(AnimHelper.getVerticalAnimation(animLayout, 500,
                    hasAnimation, layoutHeight, layoutHeight))
                hasAnimation = !hasAnimation
                true
            }
            R.id.action_member ->{
                IntentHelper.gotoEventReviewActivity(this,eventLabel,eventId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}

