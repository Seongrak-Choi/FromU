package com.fromu.fromu.data.dto

data class MailBoxViewResult(
    val coupleId: Int, // 커플 고유 넘버 아이디
    val mailboxName: String, // 우편함 이름
    val newLetterId: Int // 읽지 않은 우편물 갯수
)
