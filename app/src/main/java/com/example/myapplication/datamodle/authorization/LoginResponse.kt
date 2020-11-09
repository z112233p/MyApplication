package com.example.myapplication.datamodle.authorization

data class LoginResponse(var code :Int = 9999,
                         var msg :String = "",
                         var data :LoginResponseData = LoginResponseData())