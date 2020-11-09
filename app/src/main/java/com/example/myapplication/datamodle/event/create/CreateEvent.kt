package com.example.myapplication.datamodle.event.create

data class CreateEvent(var title: String = "",
                       var description: String = "",
                       var events_categorys_id: String = "",
                       var payment_method_id: String = "",
                       var location_title: String = "",
                       var location_gps_latitude: String = "",
                       var location_gps_longitude: String = "",
                       var meeting_title: String = "",
                       var metting_gps_latitude: String = "",
                       var metting_gps_longitude: String = "",
                       var users_limit: String = "",
                       var start_time: String = "",
                       var end_time: String = "",
                       var budget: String = "",
                       var currency_id: String = "",
                       var review_time: String = "",
                       var award_count: String = "")