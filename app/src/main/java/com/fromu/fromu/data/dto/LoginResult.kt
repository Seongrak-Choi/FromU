package com.fromu.fromu.data.dto

data class LoginResult(
    val isMember: Boolean, //true면 자동로그인 / false면 회원가입 페이지로
    val email: String?, // SNS로그인 email
    val userId: Int?, // isMember가 false 인 경우 null
    val jwt: String? // isMember가 false 인 경우 null
)
