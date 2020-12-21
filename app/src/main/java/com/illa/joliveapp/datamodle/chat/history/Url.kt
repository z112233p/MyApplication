package com.illa.joliveapp.datamodle.chat.history

data class Url(
    val headers: Headers,
    val meta: Meta,
    val parsedUrl: ParsedUrl,
    val url: String
)