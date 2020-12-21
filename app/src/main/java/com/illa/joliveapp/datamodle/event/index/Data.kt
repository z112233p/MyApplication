package com.illa.joliveapp.datamodle.event.index

data class Data(
    val incoming: List<EventIndexData>,
    val latest: List<EventIndexData>,
    val popular: List<EventIndexData>
)