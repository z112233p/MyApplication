package com.illa.joliveapp.datamodle.event.create

data class CreateEvent(var title: String = "",
                       var description: String = "",
                       var events_categorys_id: String = "",
                       var payment_method_id: String = "",
                       var location_title: String = "",
                       var location_address: String = "",
                       var location_gps_latitude: String = "",
                       var location_gps_longitude: String = "",
                       var country_id: String = "1",
                       var city_id: String = "1",
                       var users_limit: String = "",
                       var start_time: String = "",
                       var end_time: String = "",
                       var budget: String = "",
                       var currency_id: String = "",
                       var review_time: String = "",
                       var award_count: String = "",
                       var image_color: String = "",

                       var is_need_approved: String = "0")