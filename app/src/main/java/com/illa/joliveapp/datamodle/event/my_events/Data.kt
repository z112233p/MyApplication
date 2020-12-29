package com.illa.joliveapp.datamodle.event.my_events

import com.illa.joliveapp.datamodle.event.index.EventIndexData

data class Data(
    val history: List<EventIndexData>,
    val processing: List<EventIndexData>,
    val signing:  List<EventIndexData>
)