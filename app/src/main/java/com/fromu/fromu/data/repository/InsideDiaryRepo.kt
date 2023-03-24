package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.*
import com.fromu.fromu.model.listener.DetailDiaryListener
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun getDetailDiariesById(diaryId: Int): Flow<Resource<DetailDiaryRes>> {
        return diaryDataSource.getDetailDiariesById(diaryId)
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

    suspend fun editDiary(diaryId: Int, imgFile: MultipartBody.Part?, patchDiaryReq: RequestBody): Flow<Resource<EditDiaryRes>> {
        return diaryDataSource.patchDiaries(diaryId, imgFile, patchDiaryReq)
    }

    suspend fun deleteDiary(diaryId: Int): Flow<Resource<DeleteDiaryRes>> {
        return diaryDataSource.deleteDiary(diaryId)
    }
}