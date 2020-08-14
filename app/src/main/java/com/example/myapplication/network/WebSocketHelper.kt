package com.example.myapplication.network

import android.app.Activity
import android.util.Log
import com.example.myapplication.MyApp
import com.example.myapplication.`interface`.WebSocketModle
import com.example.myapplication.datamodle.chat.*
import com.example.myapplication.datamodle.chat.MessageReceived.MessageReceived
import com.example.myapplication.datamodle.chat.MessageReceived.MessageReceivedArgs
import com.example.myapplication.datamodle.chat_room.Message
import com.example.myapplication.tools.LogUtil
import com.example.myapplication.tools.PrefHelper
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject


object WebSocketHelper {

    private lateinit var webSocketGeneral: WebSocket
    private lateinit var jsonObject: JSONObject
    private lateinit var callBack: WebSocketModle
    private lateinit var mActivity: Activity



    private val webSocketListener: WebSocketListener = object : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.e("Peter", "onOpen:")

            chatConnect(
                webSocket
            )
            chatLogin(webSocket)
            subscribeMySelf(
                webSocket
            )
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.e("Peter", "onMessageB:  $bytes")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.e("Peter", "onMessageT:  $text")
            dealJsonArray(
                webSocket,
                text
            )
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            LogUtil.e(
                "Peter",
                "onFailure:  " + t.message + "  R:   " + response
            )
        }
    }

    private fun dealJsonArray(webSocket: WebSocket, jsonString: String){
        jsonObject = JSONObject(jsonString)
        if (jsonObject.has("msg")){
            when(jsonObject.get("msg")){
                "ping" -> webSocket.send(Gson().toJson(ChatPong("pong")))
                "changed" -> {
                    when(jsonObject.get("collection")){
                        "stream-notify-user" ->{

                            val argsJSONString = jsonObject.getJSONObject("fields").getJSONArray("args").get(1)
                            val messageReceivedArgs = Gson().fromJson(argsJSONString.toString(), MessageReceivedArgs::class.java)

                            if(PrefHelper.getChatRoomId() == messageReceivedArgs.lastMessage.rid &&
                                PrefHelper.getChatLable() != messageReceivedArgs.lastMessage.u.username){


                                mActivity.runOnUiThread {
                                    callBack.messageReceive(messageReceivedArgs.lastMessage.u.username,
                                        messageReceivedArgs.lastMessage.msg)
                                    Log.e("peter","stream-notify-user   IN")
                                }


                            } else {
                                Log.e("peter","stream-notify-user   Out")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun chatConnect(webSocket: WebSocket){
        val chatConnectSupport = ArrayList<String>()
        chatConnectSupport.add("1")
        chatConnectSupport.add("pre2")
        chatConnectSupport.add("pre1")
        val chatConnect = ChatConnect("connect","1",chatConnectSupport)

        webSocket.send(Gson().toJson(chatConnect))
    }

    private fun chatLogin(webSocket: WebSocket){
        val chatLoginParams = ArrayList<ChatLoginParams>()
        chatLoginParams.add(ChatLoginParams(PrefHelper.getChatToken()))
        val chatLoginData = ChatLogin("method","login",chatLoginParams,"id")

        webSocket.send(Gson().toJson(chatLoginData))
    }

    private fun subscribeMySelf(webSocket: WebSocket){
        val params: MutableList<Any>  = mutableListOf()
        params.add(PrefHelper.getChatId() +"/rooms-changed")
        params.add(false)
        val chatSubscribe = ChatSubscribe("sub","idd","stream-notify-user",params)

        webSocket.send(Gson().toJson(chatSubscribe))
    }

    fun connect(){
        val request = Request.Builder()
            .url("wss://172.19.3.98:8443/websocket")
            .build()
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .retryOnConnectionFailure(true).build()

        webSocketGeneral = client.newWebSocket(request,
            webSocketListener
        )
        client.dispatcher.executorService.shutdown()
    }

    fun stop() {
        webSocketGeneral.cancel()
    }

    fun send(content: String) {
        val data = Message()
//        webSocketGeneral.send(text)
    }

    fun sendTest(rId: String, content: String) {
        Log.e("Peter", "SEND TEST   $rId")
        val messageSendParam = MessageSendParam(java.util.UUID.randomUUID().toString(), rId, content)
        val messageSend = MessageSend("method", "sendMessage", "1", listOf(messageSendParam))

        webSocketGeneral.send(Gson().toJson(messageSend))
    }

    fun setCallBack(param: WebSocketModle){
        callBack = param
    }

    fun setAct(param: Activity){
        mActivity = param
    }
}