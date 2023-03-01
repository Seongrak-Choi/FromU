package com.fromu.fromu.data.remote.network.request

data class SignupReq(
    val email: String, // 소셜로그인 이메일
    val nickname: String, // 닉네임
    val birthday: String, // 생일
    val gender: String // 성별(M/FM)
)
