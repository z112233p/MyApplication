package com.example.myapplication.datamodle.chat.history

import java.util.*

data class Message(
    val _id: String,
    val _updatedAt: Date,
    val attachments: List<Attachment>?,
    val channels: List<Any>,
    val file: File,
    val groupable: Boolean,
    val mentions: List<Any>,
    val msg: String,
    val rid: String,
    val ts: String,
    val t: String,
    val u: U,
    val urls: List<Url>,
    val tshow: Boolean,
    val tmid: String
)