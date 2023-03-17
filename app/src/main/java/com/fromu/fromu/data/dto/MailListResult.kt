package com.fromu.fromu.data.dto

data class MailListResult(
    val letterId: Int, //편지 아이디
    val mailboxName: String, //우편함 이름
    var readFlag: Boolean, // 편지 읽었는 지
    val time: String// 편지 발송 시간
)
