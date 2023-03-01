package com.fromu.fromu.data.dto

data class LoginResult(
    val isMember: Boolean,
    val email: String?,
    val userId: Int?,
    val jwt: String?
)
