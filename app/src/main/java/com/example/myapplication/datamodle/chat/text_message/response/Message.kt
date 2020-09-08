package com.example.myapplication.datamodle.chat.text_message.response

data class Message(
    val _id: String,
    val _updatedAt: String,
    val channels: List<Any>,
    val mentions: List<Any>,
    val msg: String,
    val rid: String,
    val ts: String,
    val u: U
)