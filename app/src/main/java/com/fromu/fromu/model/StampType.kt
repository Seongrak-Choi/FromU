package com.fromu.fromu.model

import com.fromu.fromu.R
import com.fromu.fromu.data.dto.StampDto

enum class StampType(val stamp: StampDto) {
    BASIC(StampDto(0, 0, 0, R.drawable.ic_inside_paper, "")),
    FIRST(StampDto(1, R.drawable.ic_letter_stamp_1, R.drawable.ic_letter_stamp_1_size_58_58, R.drawable.ic_letter_paper_1, "프롬유의 대표적인 우표야.\n엄청난 호기심이 생기지 않니?")),
    SECOND(StampDto(2, R.drawable.ic_letter_stamp_2, R.drawable.ic_letter_stamp_2_size_58_58, R.drawable.ic_letter_paper_2, "신비함을 상징하는 우표야.\n몽환적인 보라색 편지지와 함께\n우리의 이야기를 전달해볼까?")),
    THIRD(StampDto(3, R.drawable.ic_letter_stamp_3, R.drawable.ic_letter_stamp_3_size_58_58, R.drawable.ic_letter_paper_3, "당당함을 상징하는 우표야.\n장난끼 가득한 알록달록한 \n편지지와 함께\n우리의 이야기를 적어보자!")),
    FOURTH(StampDto(4, R.drawable.ic_letter_stamp_4, R.drawable.ic_letter_stamp_4_size_58_58, R.drawable.ic_letter_paper_4, "부드러움을 상징하는 우표야.\n귀여운 분홍색의 편지지와 함께\n우리의 이야기를 전달해볼까?")),
    FIFTH(StampDto(5, R.drawable.ic_letter_stamp_5, R.drawable.ic_letter_stamp_5_size_58_58, R.drawable.ic_letter_paper_5, "희망과 자유를 상징하는 우표야.\n청량감 가득한 푸른 색의 \n편지지와 함께\n우리의 이야기를 적어볼까? ")),
    SIXTH(StampDto(6, R.drawable.ic_letter_stamp_6, R.drawable.ic_letter_stamp_6_size_58_58, R.drawable.ic_letter_paper_6, "명량함과 활기를 상징하는 우표야.\n햇살 같은 편지지와 함께\n우리의 이야기를 써 내려가볼까?"))
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

    /**
     * stampId에 맞는 explanation 반환
     */
    fun getExplanationById(stampId: Int): String {
        return StampType.values().find { it.stamp.stampId == stampId }?.stamp?.explanation ?: ""
    }

    /**
     * stampId에 맞는 stampPrice 반환
     *
     * @param stampId
     * @return
     */
    fun getStampPriceById(stampId: Int): Int {
        return StampType.values().find { it.stamp.stampId == stampId }?.stamp?.stampPrice ?: 0
    }
}