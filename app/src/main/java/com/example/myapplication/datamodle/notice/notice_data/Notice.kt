package com.example.myapplication.datamodle.notice.notice_data

data class Notice(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
)