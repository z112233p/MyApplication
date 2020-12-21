package com.illa.joliveapp.datamodle.dating

data class DatingSearchUser(
    val about: String,
    val age: Int,
    val country_id: String,
    val gender: Int,
    val id: Int,
    val job_id: String,
    val job_title: String,
    val label: String,
    val name: String,
    val photos: List<DatingSearchPhoto?>?
)