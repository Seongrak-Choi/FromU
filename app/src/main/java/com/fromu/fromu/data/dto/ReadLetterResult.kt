package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class ReadLetterResult(
    val content: String, // 편지 내용
    val letterId: Int, // 편지 아이디
    val receiveMailboxName: String, // 편지를 받은 우편함의 이름
    @SerializedName("replyFalg") val replyFlag: Boolean, // 답장 여부
    val scoreFlag: Boolean, // 별점 주었는 지 여부
    val sendMailboxName: String, // 편지를 보낸 우편함의 이름
    val stamp: Int, // 우표 타입 아이디
    val status: Int, // ??
    val time: String // 편지 작성 시간
)
