package com.example.myapplication.datamodle.chat

data class MessageSend(
    val msg: String,
    val method: String,
    val id: String,
    val params: List<MessageSendParam>
)