package com.example.myapplication.datamodle.profile.user_info

data class User(
    val about: String,
    val age: Int,
    val city_id: Any,
    val constellation_id: Int,
    val country_id: Any,
    val gender: Int,
    val id: Int,
    val instargam_images: List<Any>,
    val interest: Any,
    val interest_map: List<Int>,
    val interests: List<Interest>,
    val job_id: Int,
    val job_title: Any,
    val nickname: String,
    val photos: List<Photo>,
    val village_id: Any
)