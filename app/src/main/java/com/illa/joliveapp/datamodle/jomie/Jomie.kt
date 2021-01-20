package com.illa.joliveapp.datamodle.jomie

data class Jomie(
    val code: Int,
    val `data`: List<Data>,
    val errors: Any,
    val msg: String
)