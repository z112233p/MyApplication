package com.illa.joliveapp.datamodle.chat.image_message.response

data class Message(
    val _id: String,
    val _updatedAt: String,
    val attachments: List<Attachment>,
    val `file`: File,
    val msg: String,
    val rid: String,
    val ts: String,
    val u: U
)