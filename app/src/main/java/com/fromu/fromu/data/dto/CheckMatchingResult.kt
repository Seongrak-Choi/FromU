package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class CheckMatchingResult(
    @SerializedName("match") val isMatch: Boolean, // 매칭 여부
    val coupleRes: CoupleRes?
)

data class CoupleRes(
    val coupleId: Int, //커플 아이디(우편함 아이디)
    val nickname: String, // 우편함 이름
    val partnerNickname: String // 상대방(연인) 닉네임
)
