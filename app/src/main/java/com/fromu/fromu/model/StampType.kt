package com.fromu.fromu.model

import com.fromu.fromu.R
import com.fromu.fromu.data.dto.StampDto

enum class StampType(val stamp: StampDto) {
    BASIC(StampDto(0, 0, 0, R.drawable.ic_inside_paper)),
    FIRST(StampDto(1, R.drawable.ic_letter_stamp_1, R.drawable.ic_letter_stamp_1_size_58_58, R.drawable.ic_letter_paper_1)),
    SECOND(StampDto(2, R.drawable.ic_letter_stamp_2, R.drawable.ic_letter_stamp_2_size_58_58, R.drawable.ic_letter_paper_2)),
    THIRD(StampDto(3, R.drawable.ic_letter_stamp_3, R.drawable.ic_letter_stamp_3_size_58_58, R.drawable.ic_letter_paper_3)),
    FOURTH(StampDto(4, R.drawable.ic_letter_stamp_4, R.drawable.ic_letter_stamp_4_size_58_58, R.drawable.ic_letter_paper_4)),
    FIFTH(StampDto(5, R.drawable.ic_letter_stamp_5, R.drawable.ic_letter_stamp_5_size_58_58, R.drawable.ic_letter_paper_5)),
    SIXTH(StampDto(6, R.drawable.ic_letter_stamp_6, R.drawable.ic_letter_stamp_6_size_58_58, R.drawable.ic_letter_paper_6))
}

object FindStamp {
    /**
     * stamp 종류 리스트로 반환
     *
     * @return
     */
    fun getStampList(): ArrayList<StampDto> = ArrayList(StampType.values().map { it.stamp })

    /**
     * stampId에 맞는 StampDrawable 반환
     *
     * @param stampId
     * @return
     */
    fun getStampDrawableById(stampId: Int): Int {
        return StampType.values().find { it.stamp.stampId == stampId }?.stamp?.stampImg ?: 0
    }

    /**
     * stampId에 맞는 StampSize5858Drawable 반환
     *
     * @param stampId
     * @return
     */
    fun getStampSize5858DrawableById(stampId: Int): Int {
        return StampType.values().find { it.stamp.stampId == stampId }?.stamp?.stampImgSize5858 ?: 0
    }

    /**
     * stampId에 맞는 PaperDrawable 반환
     *
     * @param stampId
     * @return
     */
    fun getPaperDrawableById(stampId: Int): Int {
        return StampType.values().find { it.stamp.stampId == stampId }?.stamp?.paperImg ?: 0
    }
}