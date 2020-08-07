package com.example.myapplication.datamodle.authorization

data class LoginResponse(var code :Int = 0,
                         var msg :String = "",
                         var data :LoginResponseData)