package com.fromu.fromu.data.dto

data class StampDto(
    val stampId: Int, // 우표 id
    val stampImg: Int, // 우표 이미지 (size 105x145)
    val stampImgSize5858: Int, //우표 이미지 (size 58x58)
    val paperImg: Int, // 편지지 이미지
)
