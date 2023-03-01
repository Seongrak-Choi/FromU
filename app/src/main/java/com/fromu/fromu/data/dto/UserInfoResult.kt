package com.fromu.fromu.data.dto

data class UserInfoResult(
    val userId: Int, // 유저 아이디
    val nickname: String, //유저 닉네임
    val email: String, //유저 sns 이메일
    val birthday: String, //유저 생년월일
    val gender: String, //유저 성별
    val userCode: String, //유저 초대 코드
    val deleteFlag: Boolean // ??
)
