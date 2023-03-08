package com.fromu.fromu.model

import com.fromu.fromu.R
import com.fromu.fromu.data.dto.DiaryCoverDto

enum class DiaryCoverType(val diaryCoverDto: DiaryCoverDto) {
    Cover1(DiaryCoverDto(1, R.drawable.ic_diary_cover_1, R.drawable.ic_diary_cover_1_no_shadow)),
    Cover2(DiaryCoverDto(2, R.drawable.ic_diary_cover_2, R.drawable.ic_diary_cover_2_no_shadow)),
    Cover3(DiaryCoverDto(3, R.drawable.ic_diary_cover_3, R.drawable.ic_diary_cover_3_no_shadow)),
    Cover4(DiaryCoverDto(4, R.drawable.ic_diary_cover_4, R.drawable.ic_diary_cover_4_no_shadow)),
}

object FindDiaryCover {
    fun getDiaryCoverList(): ArrayList<DiaryCoverDto> = ArrayList(DiaryCoverType.values().map { it.diaryCoverDto })

    fun getShadowCoverDrawableById(coverId: Int): Int {
        return DiaryCoverType.values().find { it.diaryCoverDto.coverId == coverId }?.diaryCoverDto?.coverDrawableShadow ?: 0
    }
}