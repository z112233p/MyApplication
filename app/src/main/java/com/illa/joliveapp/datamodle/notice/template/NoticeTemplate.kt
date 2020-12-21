package com.illa.joliveapp.datamodle.notice.template

data class NoticeTemplate(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
)