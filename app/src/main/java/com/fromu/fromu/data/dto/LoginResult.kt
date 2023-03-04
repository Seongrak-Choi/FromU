package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("member") val isMember: Boolean, //true면 자동로그인 / false면 회원가입 페이지로
    val userInfo: UserInfo?
)

data class UserInfo(
    val email: String, // SNS 이메일
    val jwt: String, // 로그인 토큰
    @SerializedName("match") val isMatch: Boolean, // 유저의 커플 매칭 여부
    val refreshToken: String, // 리프레시 토큰 값
    @SerializedName("setMailboxName") val isSetMailboxName: Boolean, // 커플 매칭은 되었는데, 우편함의 이름은 정하지 않은 경우를 구분하기 위함.
    @SerializedName("userCode") val userMatchingCode: String, //유저 매칭 코드
    val userId: Int // 유저 고유 아이디
)
