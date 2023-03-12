package com.fromu.fromu.model

import com.fromu.fromu.data.dto.IndexByMonthResult

sealed class IndexInsideDiaryModel(val type: IndexInsideDiaryType) {
    data class IndexMonth(val item: String): IndexInsideDiaryModel(IndexInsideDiaryType.MONTH)
    data class IndexDay(val item: IndexByMonthResult): IndexInsideDiaryModel(IndexInsideDiaryType.DAY)
}
