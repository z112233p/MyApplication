package com.illa.joliveapp.datamodle.event.review

data class User(
    val about: Any,
    val age: Int,
    val gender: Int,
    val id: Int,
    val label: String,
    val name: String,
    val nickname: String,
    val photos: List<Photo>,
    val status: Int
)