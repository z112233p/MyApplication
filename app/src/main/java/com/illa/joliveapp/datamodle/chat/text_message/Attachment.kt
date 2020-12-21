package com.illa.joliveapp.datamodle.chat.text_message

data class Attachment(
    val audio_url: String,
    val author_icon: String,
    val author_link: String,
    val author_name: String,
    val collapsed: Boolean,
    val color: String,
    val fields: List<Field>,
    val image_url: String,
    val message_link: String,
    val text: String,
    val thumb_url: String,
    val title: String,
    val title_link: String,
    val title_link_download: Boolean,
    val ts: String,
    val video_url: String
)