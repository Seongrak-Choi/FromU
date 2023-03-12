package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.response.AllDiariesRes
import com.fromu.fromu.data.remote.network.response.ChangeFirstPageImgRes
import com.fromu.fromu.data.remote.network.response.GetMonthListRes
import com.fromu.fromu.model.listener.DetailDiaryListener
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class InsideDiaryRepo @Inject constructor(private val diaryDataSource: DiaryDataSource) {
    suspend fun getAllDiaries(diaryBookId: Int): Flow<Resource<AllDiariesRes>> {
        return diaryDataSource.getAllDiaries(diaryBookId)
    }

    fun getDetailDiariesById(diaryId: Int, listener: DetailDiaryListener) {
        return diaryDataSource.getDetailDiariesById(diaryId, listener)
    }

    suspend fun changeFirstPageImg(imgFile: MultipartBody.Part): Flow<Resource<ChangeFirstPageImgRes>> {
        return diaryDataSource.changeFirstPageImg(imgFile)
    }

    suspend fun getMonthList(diaryBookId: Int): Flow<Resource<GetMonthListRes>> {
        return diaryDataSource.getMonthList(diaryBookId)
    }
}