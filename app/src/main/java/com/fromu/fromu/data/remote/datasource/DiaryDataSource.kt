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
     * 일기 첫 페이지 조회
     *
     * @return
     */
    suspend fun getFirstPage(): Flow<Resource<FirstPageRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.getFirstPage()
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getFirstPage : An unkown error occurred"))
    }


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
     * 일기 수정
     */
    suspend fun patchDiaries(diaryId: Int, imgFile: MultipartBody.Part?, patchDiaryReq: RequestBody): Flow<Resource<EditDiaryRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.patchDiaries(diaryId, imgFile, patchDiaryReq)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("patchDiaries : An unkown error occurred"))
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
     * 일기 상세 조회(리스너 버전)
     */
    fun getDetailDiariesById(diaryId: Int, listener: DetailDiaryListener) {
        diaryService.getDetailDiaryForListener(diaryId).enqueue(object : Callback<DetailDiaryRes> {
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
     * 일기 상세 조회
     */
    fun getDetailDiariesById(diaryId: Int): Flow<Resource<DetailDiaryRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.getDetailDiary(diaryId)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getDetailDiariesById : An unkown error occurred"))
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


    /**
     * 목차 월 조회
     */
    suspend fun getMonthList(diaryBookId: Int): Flow<Resource<IndexMonthListRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.getMonthList(diaryBookId)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getMonthList : An unkown error occurred"))
    }

    /**
     * 목차 월별 작성일 조회
     */
    suspend fun getDiariesByMonth(diaryBookId: Int, month: String): Flow<Resource<IndexByMonthRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.getDiaryByMonth(diaryBookId, month)
        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("getDiariesByMonth : An unkown error occurred"))
    }


    /**
     * 일기 삭제
     *
     * @param diaryId
     * @return
     */
    suspend fun deleteDiary(diaryId: Int): Flow<Resource<DeleteDiaryRes>> = flow {
        emit(Resource.Loading)

        val res = diaryService.deleteDiary(diaryId)

        if (res.isSuccessful) {
            emit(Resource.Success(res.body()!!))
        } else {
            emit(Resource.Failed(res.message()))
        }
    }.catch {
        emit(Resource.Failed("deleteDiary : An unkown error occurred"))
    }

}