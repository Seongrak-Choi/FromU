package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class CreateDiaryResult(
    val coverNum: Int, //일기장 표지 id
    @SerializedName("diarybookId") val diaryBookId: Int, // 일기장 고유 id
    val name: String // 일기장 이름
)
