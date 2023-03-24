package com.fromu.fromu.data.dto

data class EditDiaryResult(
    val content: String, //일기 내용
    val date: String, //작성 날짜
    val day: Int, //요일
    val diaryId: Int, //일기 id
    val imageUrl: String?, //이미지 url
    val weather: String, // 감정 날씨
    val writerNickname: String // 작성자 이름
)
