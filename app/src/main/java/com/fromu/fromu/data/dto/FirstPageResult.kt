package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class FirstPageResult(
    @SerializedName("diarybookId") val diaryBookId: Int, // 일기장 아이디
    var imageUrl: String?, // 첫 페이지 대표 이미지
    val name: String, // 일기장 이름
    val writeFlag: Boolean // 일기 작성 여부
)
