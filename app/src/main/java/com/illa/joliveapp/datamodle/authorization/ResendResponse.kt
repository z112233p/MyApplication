package com.illa.joliveapp.datamodle.authorization

data class ResendResponse(
    val code: Int,
    val `data`: Data = Data(),
    val msg: String
)