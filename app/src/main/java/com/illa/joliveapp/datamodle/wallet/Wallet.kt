package com.illa.joliveapp.datamodle.wallet

data class Wallet(
    val code: Int,
    val `data`: Data,
    val errors: Any,
    val msg: String
)