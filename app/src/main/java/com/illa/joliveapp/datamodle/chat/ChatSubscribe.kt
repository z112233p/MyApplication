package com.illa.joliveapp.datamodle.chat

data class ChatSubscribe(
    val msg: String,
    val id: String,
    val name: String,
    val params: List<Any>
)