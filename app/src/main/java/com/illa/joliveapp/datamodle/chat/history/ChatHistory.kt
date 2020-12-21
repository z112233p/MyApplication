package com.illa.joliveapp.datamodle.chat.history

data class ChatHistory(
    val messages: List<Message>,
    val success: Boolean
)