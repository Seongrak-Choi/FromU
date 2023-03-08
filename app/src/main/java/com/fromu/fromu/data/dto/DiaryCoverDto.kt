package com.fromu.fromu.data.dto

data class DiaryCoverDto(
    val coverId: Int, // 커버 아이디
    val coverDrawableShadow: Int, //커버 이미지 그림자 있는 리소스
    val coverDrawableNoShadow: Int // 커버 이미지 그림자 없는 리소스
)
