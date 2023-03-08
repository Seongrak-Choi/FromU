package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.DiaryService
import com.fromu.fromu.data.remote.network.request.PostDiaryBookReq
import com.fromu.fromu.data.remote.network.response.PatchDiaryBooksPassRes
import com.fromu.fromu.data.remote.network.response.PostDiaryBookRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DiaryDataSource @Inject constructor(
    private val diaryService: DiaryService,
) {

    /**
     *일기장 등록(생성)
     */
    suspend fun postDiaryBook(postDiaryBookReq: PostDiaryBookReq): Flow<Resource<PostDiaryBookRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.postDiaryBook(postDiaryBookReq)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("postDiaryBook : An unkown error occurred"))
    }

    suspend fun patchDiaryBooksPass(): Flow<Resource<PatchDiaryBooksPassRes>> = flow<Resource<PatchDiaryBooksPassRes>> {
        emit(Resource.Loading)

        val res = diaryService.patchDiaryBooksPss()
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("postDiaryBook : An unkown error occurred"))
    }
}