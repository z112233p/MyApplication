package com.illa.joliveapp.datamodle.event.my_events

data class Processing(
    val author: Author,
    val end_time: String,
    val image: String,
    val joins: List<Join>,
    val label: String,
    val location_title: String,
    val start_time: String,
    val title: String
)