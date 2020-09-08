package com.example.myapplication.datamodle.chat.MessageReceived

data class LastMessage(
    val _id: String,
    val _updatedAt: UpdatedAtX,
    val channels: List<Any>,
    val mentions: List<Any>,
    val file: Any,
    val msg: String,
    val rid: String,
    val ts: Ts,
    val u: U
)