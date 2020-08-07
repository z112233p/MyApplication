package com.example.myapplication.datamodle.authorization

data class LoginResponseData(var label: String = "",
                             var user_id: Int = 0,
                             var user_token: String = "",
                             var chat_user_id: String = "", 
                             var chat_auth_token: String = "")