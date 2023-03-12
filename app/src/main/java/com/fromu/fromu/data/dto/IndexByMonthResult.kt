package com.fromu.fromu.data.dto

data class IndexByMonthResult(
    val month: String,
    val diaryInfoList: ArrayList<IndexDiaryInfo>
)

data class IndexDiaryInfo(
    val content: String, // 일기 제목
    val date: String, // 작성 날짜
    val diaryId: Int // 일기 id
)

