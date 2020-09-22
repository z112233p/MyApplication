package com.example.myapplication.datamodle.profile.update

data class User(
    val about: Any,
    val age: Int,
    val birthday: String,
    val city_id: Any,
    val constellation_id: Int,
    val country_id: Any,
    val gender: Int,
    val geo: Geo,
    val gold: Gold,
    val id: Int,
    val interest_map: List<Int>,
    val interests: List<Interest>,
    val job_id: Any,
    val job_title: Any,
    val label: String,
    val language_id: Int,
    val name: String,
    val phone: String,
    val role_id: Int,
    val stamina: Int,
    val village_id: Any
)