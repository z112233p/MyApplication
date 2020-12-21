package com.illa.joliveapp.datamodle.chat.MessageReceived

data class MessageReceived(
    val collection: String,
    val fields: MessageReceivedFields,
    val id: String,
    val msg: String
)