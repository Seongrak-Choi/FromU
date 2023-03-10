package com.fromu.fromu.data.remote.datasource

import com.fromu.fromu.data.remote.network.Resource
import com.fromu.fromu.data.remote.network.api.DiaryService
import com.fromu.fromu.data.remote.network.request.CreateDiaryBookReq
import com.fromu.fromu.data.remote.network.response.*
import com.fromu.fromu.model.listener.DetailDiaryListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DiaryDataSource @Inject constructor(
    private val diaryService: DiaryService,
) {

    /**
     *일기장 등록(생성)
     */
    suspend fun createDiaryBook(createDiaryBookReq: CreateDiaryBookReq): Flow<Resource<CreateDiaryBookRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.postDiaryBook(createDiaryBookReq)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("createDiaryBook : An unkown error occurred"))
    }

    /**
     * 일기장 보내기
     *
     * @return
     */
    suspend fun sendDiaryBooks(): Flow<Resource<SendDiaryBooksRes>> = flow<Resource<SendDiaryBooksRes>> {
        emit(Resource.Loading)

        val res = diaryService.patchDiaryBooksPss()
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("sendDiaryBooks : An unkown error occurred"))
    }

    /**
     * 일기 등록 api
     */
    suspend fun writeDiary(imgFile: MultipartBody.Part, data: RequestBody): Flow<Resource<WriteDiaryRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.postDiaries(imgFile, data)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("writeDiary : An unkown error occurred"))
    }


    /**
     * 일기 전체 조회
     */
    suspend fun getAllDiaries(diaryBookId: Int): Flow<Resource<AllDiariesRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.getAllDiary(diaryBookId)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getAllDiary : An unkown error occurred"))
    }


    /**
     * 일기 상세 조회
     */
    fun getDetailDiariesById(diaryId: Int, listener: DetailDiaryListener) {
        diaryService.getDetailDiary(diaryId).enqueue(object : Callback<DetailDiaryRes> {
            override fun onResponse(call: Call<DetailDiaryRes>, response: Response<DetailDiaryRes>) {
                response.body()?.let {
                    listener.onSuccess(it)
                } ?: let {
                    listener.onFailure(Throwable("Network error"))
                }
            }

            override fun onFailure(call: Call<DetailDiaryRes>, t: Throwable) {
                listener.onFailure(t)
            }
        })
    }

    /**
     * 일기장 내지 첫장 대표 이미지 변경
     */
    suspend fun changeFirstPageImg(imgFile: MultipartBody.Part): Flow<Resource<ChangeFirstPageImgRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.patchDiaryBooksImage(imgFile)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("changeFirstPageImg : An unkown error occurred"))
    }
}