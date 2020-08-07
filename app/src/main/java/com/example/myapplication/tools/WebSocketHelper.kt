package com.example.myapplication.tools

import android.util.Log
import com.example.myapplication.datamodle.chat.ChatConnect
import com.example.myapplication.datamodle.chat.ChatLogin
import com.example.myapplication.datamodle.chat.ChatLoginParams
import com.example.myapplication.rx.UnsafeOkHttpClient
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


object WebSocketHelper {

    private var webSocketGeneral: WebSocket? = null
    private val webSocketListener: WebSocketListener = object : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.e("Peter", "onOpen:")

            chatConnect(webSocket)
            chatLogin(webSocket)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.e("Peter", "socket0:  $bytes")

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.e("Peter", "socket1:  $text")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("Peter", "onFailure:  "+t.message+"  R:   "+response)

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

    fun connect(){
        val request = Request.Builder()
            .url("wss://172.19.3.98:8443/websocket")
            .build()
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .retryOnConnectionFailure(true).build()

        client.newWebSocket(request, webSocketListener)
        client.dispatcher.executorService.shutdown()
    }

    fun stop() {
        if (webSocketGeneral != null) {
            webSocketGeneral!!.cancel()
        }
    }

    fun send(content: String) {
        if (webSocketGeneral == null) {
            return
        }
        val text= if (content.contains("\"type\":")) {
            content
        } else {
            String.format("{\"type\":\"1\", \"content\":\"%s\"}", content)
        }
        webSocketGeneral?.send(text)
    }

    fun sendTest(content: String) {
        if (webSocketGeneral == null) {
            return
        }

        webSocketGeneral?.send(content)
    }
}