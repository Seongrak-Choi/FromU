package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class CheckMatchingResult(
    @SerializedName("match") val isMatch: Boolean, // 매칭 여부
    val dday: Int, // 디데이
    val coupleRes: CoupleRes?
)

data class CoupleRes(
    val coupleId: Int, //커플 아이디(우편함 아이디)
    val nickname: String, // 우편함 이름
    val partnerNickname: String, // 상대방(연인) 닉네임
    val setMailboxName: Boolean // 우편함 이름 정했는 지 true = 정함, false = 안 정함
)
