package com.example.myapplication.datamodle.chat

data class ChatLogin(val msg: String = "method",
                     val method: String = "login",
                     var params: ArrayList<ChatLoginParams>?,
                     var id: String = "")