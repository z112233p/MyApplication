package com.example.myapplication.datamodle.authorization.register

data class RegisterResponse(
    val code: Int,
    val `data`: Data?,
    val msg: String, var errors: Any?
)