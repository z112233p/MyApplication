package com.illa.joliveapp.datamodle


data class ErrorLogApi (var app_name: String = "SMTV",
                        var os: String = "ANDROID",
                        var version: String = "1.0.0",
                        var code: Int = 1,
                        var client_ip: String = "0",
                        var comment: String = "",
                        var up_datetime: String = "2002-07-09 10:50:30")