package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class DiaryViewResult(
    val dday: Int?,
    @SerializedName("diarybook") val diaryBook: DiaryBook?, //일기장 정보
    @SerializedName("diarybookStatus") val diaryBookStatus: Int, //일기장 상태
    @SerializedName("nickname") val nickName: String, //자신 닉네임
    @SerializedName("partnerNickname") val partnerNickname: String, //상대방 닉네임
)

data class DiaryBook(
    val coverNum: Int, //일기장 표지 아이디
    @SerializedName("diarybookId") val diaryBookId: Int,
    val imageUrl: String,
    val name: String,
    val writeFlag: Boolean //일기를 작성 하였는지
)
