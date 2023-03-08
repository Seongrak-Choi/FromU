package com.fromu.fromu.data.remote.network.request

data class PostDiaryBookReq(
    val coverNum: Int, // 표지 id
    val name: String // 일기장 이름
)
