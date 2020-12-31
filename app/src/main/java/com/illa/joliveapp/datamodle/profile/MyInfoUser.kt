package com.illa.joliveapp.datamodle.profile

data class MyInfoUser(
    var id:Int = 0,
    var name:String = "",
    var nickname: String = "",
    var phone: String = "",
    var code:Int = 0,
    var gender:Int = 0,
    var birthday: String = "",
    var about:String = "",
    var role_id: Int = 0,
    var job_title:String = "",
    var job_id: String = "",
    var language_id:Int = 0,
    var country_id: String = "",
    var city_id:String = "",
    var village_id: String = "",
    var age:String = "",
    var constellation_id: Int = 0,
    var label:String = "",
    var stamina: String = "",
    var interest_map: Array<Int>? = null,
    var photos: ArrayList<MyInfoPhoto>? = null,
    var gold: MyInfoGold,
    var geo: MyInfoGeo,
    var follows_count: Int = 0,
    var fans_count: Int = 0,
    var is_follow: Boolean = false,
    var interests:ArrayList<MyInfoInterest>? = null)