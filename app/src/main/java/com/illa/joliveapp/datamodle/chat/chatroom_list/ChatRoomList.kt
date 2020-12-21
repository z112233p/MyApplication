package com.illa.joliveapp.datamodle.chat.chatroom_list

data class ChatRoomList(
    val remove: List<Any>,
    val success: Boolean,
    val update: List<Update>
)