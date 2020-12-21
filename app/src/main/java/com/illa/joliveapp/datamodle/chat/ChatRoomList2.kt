package com.illa.joliveapp.datamodle.chat

data class ChatRoomList2(
    val remove: List<Any>,
    val success: Boolean,
    val update: List<ChatRoomListUpdate>
)