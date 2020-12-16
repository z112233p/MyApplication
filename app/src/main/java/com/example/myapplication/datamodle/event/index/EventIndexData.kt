package com.example.myapplication.datamodle.event.index

data class EventIndexData(
    val author: Author,
    val budget: Int,
    val category: List<String>,
    val city_id: Int,
    val country_id: Int,
    val description: String,
    val events_categorys_id: Int,
    val id: Int,
    val image: String,
    val is_active: Boolean,
    val is_already_attend: Boolean,
    val joins: List<Join>,
    val label: String,
    val location_address: String,
    val location_gps_latitude: String,
    val location_gps_longitude: String,
    val location_title: String,
    val map_url: Any,
    val popular: Int,
    val start_time: String,
    val tags: String,
    val title: String,
    val user_id: Int,
    val users_limit: Int,
    val vip: Any
)