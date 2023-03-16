package com.fromu.fromu.data.remote.network.request

data class PostLetterReq(
    val content: String, // 편지 내용
    val stampNum: Int // 우표 아이디
)
