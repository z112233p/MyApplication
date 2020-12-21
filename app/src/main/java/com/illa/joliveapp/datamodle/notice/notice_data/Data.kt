package com.illa.joliveapp.datamodle.notice.notice_data

data class Data(
    val _id: Id,
    val args: List<String>,
    val created_at: String,
    val is_read: Boolean,
    val navigation: Navigation,
    val template: String
)