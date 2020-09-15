package com.example.myapplication.datamodle.chat.MessageReceived.message_received_args

data class LastMessage(
    val _id: String,
    val _updatedAt: UpdatedAtX,
    val attachments: List<Attachment>,
    val channels: List<Any>,
    val `file`: File = File(),
    val groupable: Boolean,
    val mentions: List<Any>,
    val msg: String,
    val rid: String,
    val ts: Ts,
    val u: U
)