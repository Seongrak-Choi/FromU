package com.fromu.fromu.data.dto

data class SignupResult(
    val userId: Int,
    val refreshToken: String,
    val userCode: String,
    val jwt: String
)
