package com.illa.joliveapp.datamodle.chat.chatroom_list

data class Attachment(
    val audio_size: Int,
    val audio_type: String,
    val audio_url: String,
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