package com.fromu.fromu.data.dto

import com.google.gson.annotations.SerializedName

data class ReadLetterResult(
    val content: String, // 편지 내용
    val letterId: Int, // 편지 아이디
    val receiveMailboxName: String, // 편지를 받은 우편함의 이름

    /*
     true = 답장 함 / false = 답장 안 함
     */
    @SerializedName("replyFalg") val replyFlag: Boolean, // 답장 여부 true이면 답장 한거, flase이면 아직 답장 안한거

    /*
     true = 별점 줌 / false = 별점 아직 안 줌
    */
    val scoreFlag: Boolean, // 별점 주었는 지 여부

    val sendMailboxName: String, // 편지를 보낸 우편함의 이름
    val stamp: Int, // 우표 타입 아이디

    /*
    * status = 0 / 받은 편지의 경우
    * status = 1 / 보낸 편지의 경우
    * status = 2 / 내가 보낸 편지의 답장인 경우
    */
    val status: Int,
    val time: String // 편지 작성 시간
)
