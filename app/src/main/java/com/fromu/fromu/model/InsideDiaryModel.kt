package com.fromu.fromu.model

import com.fromu.fromu.data.dto.DetailDiaryResult
import com.fromu.fromu.data.dto.DiaryBook

sealed class InsideDiaryModel(val type: InsideDiaryType) {
    data class Header(val item: DiaryBook) : InsideDiaryModel(InsideDiaryType.HEADER)
    data class Diaries(val item: DetailDiaryResult) : InsideDiaryModel(InsideDiaryType.DIARY)
}
