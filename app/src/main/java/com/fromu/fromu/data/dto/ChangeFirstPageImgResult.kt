package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class ChangeFirstPageImgResult(
    @SerializedName("diarybookId") val diaryBookId: Int, //일기장 아이디
    val userId: Int // 유저 아이디
)
