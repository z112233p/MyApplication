package com.example.myapplication.datamodle.chat

data class ChatRoomList(
    val remove: List<Any>,
    val success: Boolean,
    val update: List<ChatRoomListUpdate>
)