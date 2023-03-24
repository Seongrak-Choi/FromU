package com.fromu.fromu.data.dto

data class DetailDiaryResult(
    val content: String? = null, // 일기 내용
    val date: String? = null, // 작성 날짜
    val day: Int = 0, // 요일 1(월요일) ~ 7(일요일)
    val diaryId: Int, // 일기 id
    val imageUrl: String? = "", //일기에 첨부한 이미지
    val weather: String = "", // 감정 날씨
    val writerNickname: String? = null, //작성자 닉네임
    val writerUserId: Int? = null //작성자 유저 id
)
