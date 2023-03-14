package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.*
import com.fromu.fromu.model.listener.DetailDiaryListener
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class InsideDiaryRepo @Inject constructor(private val diaryDataSource: DiaryDataSource) {

    suspend fun getFirstPage(): Flow<Resource<FirstPageRes>> {
        return diaryDataSource.getFirstPage()
    }

    suspend fun getAllDiaries(diaryBookId: Int): Flow<Resource<AllDiariesRes>> {
        return diaryDataSource.getAllDiaries(diaryBookId)
    }

    fun getDetailDiariesById(diaryId: Int, listener: DetailDiaryListener) {
        return diaryDataSource.getDetailDiariesById(diaryId, listener)
    }

    suspend fun changeFirstPageImg(imgFile: MultipartBody.Part): Flow<Resource<ChangeFirstPageImgRes>> {
        return diaryDataSource.changeFirstPageImg(imgFile)
    }

    suspend fun getMonthList(diaryBookId: Int): Flow<Resource<IndexMonthListRes>> {
        return diaryDataSource.getMonthList(diaryBookId)
    }

    suspend fun getDiariesByMonth(diaryBookId: Int, month: String): Flow<Resource<IndexByMonthRes>> {
        return diaryDataSource.getDiariesByMonth(diaryBookId, month)
    }
}