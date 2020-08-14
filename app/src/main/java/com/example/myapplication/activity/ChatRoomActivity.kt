package com.example.myapplication.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.`interface`.WebSocketModle
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.tools.PrefHelper
import com.example.myapplication.network.WebSocketHelper
import com.example.myapplication.viewmodle.ChatRoomActivityVM
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_chat_room.*
import okhttp3.WebSocket
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatRoomActivity : AppCompatActivity(), WebSocketModle {

    private val chatRoomActVM: ChatRoomActivityVM by viewModel()

    private lateinit var rId: String
    private lateinit var adapter: MessagesListAdapter<Message>
    private lateinit var dataList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        dataList = ArrayList()
        rId = ""
        getIntentData()
        init()
        initObserve()
        chatRoomActVM.getChatHistory(rId)
    }

    private fun getIntentData(){
        val b = intent.extras
        rId = b?.getString("ChatRoomID")!!
        PrefHelper.setChatRoomId(rId)
    }

    private fun init(){
        Log.e("peter","ChatRoomActivity")
        WebSocketHelper.setCallBack(this)
        WebSocketHelper.setAct(this)
        val imageLoader: ImageLoader = object : ImageLoader {
            override fun loadImage(imageView: ImageView?, url: String?, payLoad: Any?) {
                Picasso.with(this@ChatRoomActivity).load(url).into(imageView)
            }
        }
        adapter = MessagesListAdapter<Message>(PrefHelper.getChatLable(), imageLoader)
        messagesList.setAdapter(adapter)
//        test()

        input.setInputListener {
            val data = Message()
            Log.e("Peter", "setInputListener    $it")
            data.text = it.toString()
            WebSocketHelper.sendTest(rId,it.toString())
            adapter.addToStart(data,true)
            return@setInputListener true
        }
    }

    override fun messageReceive(label: String, text: String) {
        Log.e("peter", "messageReceive   IN$label    $text")

        val data = Message()
        data.author.id = label
        data.text = text
        data.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+label+".jpg"
        adapter.addToStart(data,true)
    }


    private fun initObserve(){
        chatRoomActVM.getChatHistoryData().observe(this, Observer { list  ->
            list.forEach { it ->

                if(!TextUtils.isEmpty(it.u.name)){
                    val data = Message()
                    data.createdAt = it._updatedAt
                    data.author.id = it.u.username
                    data.text = it.msg
                    data.author.avatar = BuildConfig.IMAGE_URL+"chatroom/dating/"+it.u.username+".jpg"
                    Log.e("Peter","AVAICON:   "+data.author.avatar)
                    dataList.add(0,data)

                }

            }
            dataList.forEach{
                adapter.addToStart(it,true)

            }
        })
    }

    private fun test(){
        val dataList = Message()
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList,true)

        val dataList2 = Message()
        dataList2.id = "YOYO"
        dataList2.user.id = "YOYO"
        Log.e("peter","SENDID:  "+dataList2.id)
        adapter.addToStart(dataList2,true)
        adapter.addToStart(dataList2,false)
        adapter.addToStart(dataList,false)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList2,true)
        adapter.addToStart(dataList2,false)
        adapter.addToStart(dataList,false)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList2,true)
        adapter.addToStart(dataList2,false)
        adapter.addToStart(dataList,false)
        adapter.addToStart(dataList,true)
        adapter.addToStart(dataList2,true)
        adapter.addToStart(dataList2,false)
        adapter.addToStart(dataList,false)
        adapter.addToStart(dataList,true)
    }


}