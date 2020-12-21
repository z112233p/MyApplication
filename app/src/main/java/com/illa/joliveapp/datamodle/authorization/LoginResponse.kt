package com.illa.joliveapp.datamodle.authorization

data class LoginResponse(var code :Int = 9999,
                         var msg :String = "",
                         var data :LoginResponseData = LoginResponseData())