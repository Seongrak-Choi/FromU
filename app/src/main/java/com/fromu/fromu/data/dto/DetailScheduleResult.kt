package com.fromu.fromu.data.dto

data class DetailScheduleResult(
    val content: String, // 일정 내용
    val date: String, // 날짜
    val editable: Boolean, // 수정 가능 여부(기념일이라면 false 유저가 등록한 일정이면 true)
    val nickname: String, // 일정 등록한 유저의 닉네임 (기념일이라면 닉네임이 우리)
    val scheduleId: Int, // 일정 아이디
    val userId: Int // 일정 등록한 유저의 아이디
)
