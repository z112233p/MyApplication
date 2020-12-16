package com.example.myapplication.datamodle.event.my_events

import com.example.myapplication.datamodle.event.index.EventIndexData

data class Data(
    val history: List<EventIndexData>,
    val processing: List<EventIndexData>
)