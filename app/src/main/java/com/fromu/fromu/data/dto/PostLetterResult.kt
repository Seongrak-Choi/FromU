package com.fromu.fromu.data.dto

data class PostLetterResult(
    val letterId: Int, // 편지 아이디
    val receiveMailboxName: String, //받는 우편함 이름
    val sendMailboxName: String // 보낸 우편함 이름
)
