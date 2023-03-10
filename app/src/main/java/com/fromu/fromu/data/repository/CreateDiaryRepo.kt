package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.CreateDiaryBookReq
import com.fromu.fromu.data.remote.network.response.CreateDiaryBookRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateDiaryRepo @Inject constructor(private val diaryDataSource: DiaryDataSource) {

    /**
     *일기장 등록(생성)
     */
    suspend fun postDiaryBook(createDiaryBookReq: CreateDiaryBookReq): Flow<Resource<CreateDiaryBookRes>> {
        return diaryDataSource.createDiaryBook(createDiaryBookReq)
    }
}