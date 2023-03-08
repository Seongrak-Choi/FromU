package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class PatchDiaryBooksPassResult(
    @SerializedName("diarybookId") val diaryBookId: Int,
    val userId: Int
)
