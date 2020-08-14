package com.example.myapplication.datamodle.chat

data class ChatRoomListLastMessage(
    val _id: String,
    val _updatedAt: String,
    val channels: List<Any>,
    val mentions: List<Any>,
    val msg: String,
    val rid: String,
    val ts: String,
    val u: ChatRoomListU
)