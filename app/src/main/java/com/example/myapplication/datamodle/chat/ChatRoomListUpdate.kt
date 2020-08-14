package com.example.myapplication.datamodle.chat

data class ChatRoomListUpdate(
    val _id: String,
    val _updatedAt: String,
    val customFields: ChatRoomListCustomFields,
    val default: Boolean,
    val fname: String,
    val lastMessage: ChatRoomListLastMessage,
    val lm: String,
    val name: String,
    val ro: Boolean,
    val sysMes: Boolean,
    val t: String,
    val topic: String,
    val u: ChatRoomListUX,
    val usernames: List<Any>,
    val usersCount: Int
)