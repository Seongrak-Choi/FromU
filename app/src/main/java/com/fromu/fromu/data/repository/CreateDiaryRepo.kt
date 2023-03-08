package com.fromu.fromu.data.repository

import com.fromu.fromu.data.remote.datasource.DiaryDataSource
import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.request.PostDiaryBookReq
import com.fromu.fromu.data.remote.network.response.PostDiaryBookRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateDiaryRepo @Inject constructor(private val diaryDataSource: DiaryDataSource) {

    /**
     *일기장 등록(생성)
     */
    suspend fun postDiaryBook(postDiaryBookReq: PostDiaryBookReq): Flow<Resource<PostDiaryBookRes>> {
        return diaryDataSource.postDiaryBook(postDiaryBookReq)
    }
}