package com.example.myapplication.datamodle.chat

data class ChatRoomList2(
    val remove: List<Any>,
    val success: Boolean,
    val update: List<ChatRoomListUpdate>
)