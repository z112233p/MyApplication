package com.example.myapplication.datamodle.chat.chatroom_list

import java.util.*

data class LastMessage(
    val _id: String,
    val _updatedAt: Date,
    val attachments: List<Attachment>,
    val channels: List<Any>,
    val `file`: File,
    val groupable: Boolean,
    val mentions: List<Any>,
    val msg: String,
    val rid: String,
    val ts: String,
    val u: U
)