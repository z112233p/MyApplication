package com.example.myapplication.datamodle.chat.chatroom_list

data class Update(
    val _id: String,
    val _updatedAt: String,
    val customFields: CustomFields,
    val default: Boolean,
    val fname: String,
    val lastMessage: LastMessage,
    val lm: String,
    val name: String,
    val ro: Boolean,
    val sysMes: Boolean,
    val t: String,
    val topic: String,
    val u: UX,
    val usernames: List<Any>,
    val usersCount: Int
)