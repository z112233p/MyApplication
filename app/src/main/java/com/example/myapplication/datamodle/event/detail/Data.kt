package com.example.myapplication.datamodle.event.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val award_count: Int,
    val back_award: Int,
    val budget: Int,
    val chat_rid: String,
    val city_id: Int,
    val country_id: Int,
    val created_at: String,
    val currency_id: Int,
    val deleted_at: String,
    val description: String,
    val end_time: String,
    val events_categorys_id: Int,
    val id: Int,
    val image: String,
    var join_type: String = "",
    val label: String,
    val location_gps_latitude: String,
    val location_gps_longitude: String,
    val location_title: String,
    val meeting_title: String,
    val metting_gps_latitude: String,
    val metting_gps_longitude: String,
    val payment_method_id: Int,
    val review_time: String,
    val start_time: String,
    val title: String,
    val updated_at: String,
    val user_id: Int,
    val users_limit: Int,
    val location_address: String
): Parcelable