package com.example.myapplication.datamodle.chat.history

data class ParsedUrl(
    val hash: Any,
    val host: String,
    val hostname: String,
    val pathname: String,
    val port: Any,
    val protocol: String,
    val query: Any,
    val search: Any
)