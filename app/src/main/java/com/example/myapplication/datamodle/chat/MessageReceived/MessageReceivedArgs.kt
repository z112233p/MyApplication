package com.example.myapplication.datamodle.chat.MessageReceived

data class MessageReceivedArgs(
    val _id: String,
    val _updatedAt: UpdatedAt,
    val customFields: CustomFields,
    val default: Boolean,
    val fname: String,
    val lastMessage: LastMessage,
    val lm: Lm,
    val name: String,
    val ro: Boolean,
    val sysMes: Boolean,
    val t: String,
    val topic: String,
    val u: UX,
    val usersCount: Int
)