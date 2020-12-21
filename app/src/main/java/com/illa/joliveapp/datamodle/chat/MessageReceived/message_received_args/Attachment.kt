package com.illa.joliveapp.datamodle.chat.MessageReceived.message_received_args

data class Attachment(
    val image_preview: String,
    val image_size: Int,
    val image_type: String,
    val image_url: String,
    val title: String,
    val title_link: String,
    val title_link_download: Boolean,
    val ts: String,
    val type: String
)