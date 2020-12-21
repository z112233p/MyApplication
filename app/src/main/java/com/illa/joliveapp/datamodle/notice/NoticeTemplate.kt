package com.illa.joliveapp.datamodle.notice

data class NoticeTemplate(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
)