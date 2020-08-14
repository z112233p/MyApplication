package com.example.myapplication.datamodle.chat.history

data class ChatHistory(
    val messages: List<Message>,
    val success: Boolean
)