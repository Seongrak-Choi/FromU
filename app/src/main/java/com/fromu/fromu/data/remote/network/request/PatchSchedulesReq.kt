package com.fromu.fromu.data.remote.network.request

data class PatchSchedulesReq(
    val content: String, // 일정 내용
    val date: String // 일정 등록 날
)
